package org.springframework.samples.petclinic.view;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

public class JsonViewResolver implements ViewResolver {
    /**
      * Get the view to use.
      *
      * @return Always returns an instance of {@link MappingJacksonJsonView}.
     */
    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setPrettyPrint(true);      // Lay the JSON out to be nicely readable 
        return view;
    }
}