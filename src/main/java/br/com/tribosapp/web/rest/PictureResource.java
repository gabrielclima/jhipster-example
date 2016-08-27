package br.com.tribosapp.web.rest;

import br.com.tribosapp.config.Constants;
import br.com.tribosapp.domain.Picture;
import br.com.tribosapp.domain.enumeration.PictureType;
import br.com.tribosapp.repository.PictureRepository;
import br.com.tribosapp.web.rest.util.HeaderUtil;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.codahale.metrics.annotation.Timed;
import org.apache.commons.io.FileUtils;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Picture.
 */
@RestController
@RequestMapping("/api")
public class PictureResource {

    private final Logger log = LoggerFactory.getLogger(PictureResource.class);

    @Inject
    private PictureRepository pictureRepository;

    @Inject
    private Environment env;

    /**
     * POST /pictures : Create a new picture.
     *
     * @param multipartFile the file to upload
     * @param pictureTitle  the picture title
     * @param description   the picture short description
     * @param type          the picture type
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new picture, or with status 400 (Bad Request) if the picture has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pictures/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Picture> uploadPicture(@RequestParam(name = "file") MultipartFile multipartFile,
                                                 @RequestParam(name = "pictureTitle") String pictureTitle,
                                                 @RequestParam(name = "description") String description,
                                                 @RequestParam(name = "type") String type) throws URISyntaxException {

        log.debug("REST request to save Picture : {}", pictureTitle);

        if (multipartFile.isEmpty()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("picture", "filenotfound", "The file has not been loaded"))
                .body(null);
        }

        Picture picture = new Picture();

        // Add automatic parameters
        picture.setCreatedAt(LocalDate.now());
        picture.setUpdatedAt(picture.getCreatedAt());
        picture.setFile(Picture.generateFilename(multipartFile.getOriginalFilename()));
        picture.setType(PictureType.valueOf(type));
        picture.setPictureTitle(pictureTitle);
        picture.setDescription(description);

        try {
            System.out.println("Uploading a new object to S3 from a file\n");

            // store file in S3 Bucket
            AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();

            AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
            String bucketName = Constants.AWS_S3_BUCKET;
            String keyName = Constants.AWS_FOLDER_IMG + type.toString().toLowerCase() + "/" + picture.getFile();

            s3client.putObject(new PutObjectRequest(bucketName, keyName, multipartFile.getInputStream(), new ObjectMetadata()));

        } catch (IOException e) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("picture", "errorsavingfile", "Error saving file."))
                .body(null);
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
                "means your request made it " +
                "to Amazon S3, but was rejected with an error response " +
                "for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
                "means the client encountered " +
                "an internal error while trying to " +
                "communicate with S3, " +
                "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }

        //Código para salvar o arquivo na máquina local
        /*try {
            FileUtils.writeByteArrayToFile(new File(Picture.FOLDER + picture.getFile()), multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("picture", "ioexception", e.getMessage())).body(null);
        }*/

        Picture result = pictureRepository.save(picture);
        return ResponseEntity.created(new URI("/api/pictures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("picture", result.getId().toString())).body(result);
    }

    // /**
    // * PUT /pictures : Updates an existing picture.
    // *
    // * @param picture
    // * the picture to update
    // * @return the ResponseEntity with status 200 (OK) and with body the
    // updated
    // * picture, or with status 400 (Bad Request) if the picture is not
    // * valid, or with status 500 (Internal Server Error) if the picture
    // * couldnt be updated
    // * @throws URISyntaxException
    // * if the Location URI syntax is incorrect
    // */
    // @RequestMapping(value = "/pictures", method = RequestMethod.PUT, produces
    // = MediaType.APPLICATION_JSON_VALUE)
    // @Timed
    // public ResponseEntity<Picture> updatePicture(@RequestParam("file")
    // MultipartFile file, @RequestBody Picture picture)
    // throws URISyntaxException {
    // log.debug("REST request to update Picture : {}", picture);
    // if (picture.getId() == null) {
    // return createPicture(file, picture);
    // }
    // Picture result = pictureRepository.save(picture);
    // return
    // ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("picture",
    // picture.getId().toString()))
    // .body(result);
    // }

    /**
     * GET /pictures : get all the pictures.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pictures
     * in body
     */
    @RequestMapping(value = "/pictures", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Picture> getAllPictures() {
        log.debug("REST request to get all Pictures");
        List<Picture> pictures = pictureRepository.findAll();
        return pictures;
    }

    /**
     * GET /pictures/:id : get the "id" picture.
     *
     * @param id the id of the picture to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     * picture, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/pictures/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Picture> getPicture(@PathVariable Long id) {
        log.debug("REST request to get Picture : {}", id);
        Picture picture = pictureRepository.findOne(id);


        return Optional.ofNullable(picture).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /pictures/:id : delete the "id" picture.
     *
     * @param id the id of the picture to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/pictures/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePicture(@PathVariable Long id) {
        log.debug("REST request to delete Picture : {}", id);
        pictureRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("picture", id.toString())).build();
    }

}
