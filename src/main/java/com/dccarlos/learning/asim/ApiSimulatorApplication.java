package com.dccarlos.learning.asim;

import com.dccarlos.learning.asim.resources.KISResourceV1;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ApiSimulatorApplication extends Application<ApiSimulatorConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ApiSimulatorApplication().run(args);
    }

    @Override
    public String getName() {
        return "ApiSimulator";
    }

    @Override
    public void initialize(final Bootstrap<ApiSimulatorConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/static", "/", "index.html", "api-simulator-ui"));
    }

    @Override
    public void run(final ApiSimulatorConfiguration configuration, final Environment environment) {
        environment.jersey().register(KISResourceV1.builder().build());
    }
}