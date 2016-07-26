package br.com.tribosapp.cucumber.stepdefs;

import br.com.tribosapp.TribosApp;

import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

@WebAppConfiguration
@ContextConfiguration(classes = TribosApp.class, loader = SpringApplicationContextLoader.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
