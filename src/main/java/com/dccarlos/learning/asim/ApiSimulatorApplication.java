package com.dccarlos.learning.asim;

import org.eclipse.jetty.server.session.SessionHandler;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.dccarlos.learning.asim.resources.KISResourceV1;
import com.dccarlos.learning.asim.resources.security.KISBasicAuthenticator;
import com.dccarlos.learning.asim.resources.security.KISUnauthorizedHandler;
import com.dccarlos.learning.asim.resources.security.KisUser;
import com.google.common.cache.CacheBuilderSpec;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.CachingAuthenticator;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.basic.BasicCredentials;
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
        prepareAuthenticator(environment);
    }
    
    private void prepareAuthenticator(Environment env) {
        env.servlets().setSessionHandler(new SessionHandler());

        Authenticator<BasicCredentials, KisUser> authenticator =
                new CachingAuthenticator<>(
                        env.metrics(),
                        KISBasicAuthenticator.builder().build(),
                        CacheBuilderSpec.parse("maximumSize=10000, expireAfterWrite=10m"));

        env.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<KisUser>()
                    .setAuthenticator(authenticator)
                    .setUnauthorizedHandler(new KISUnauthorizedHandler())
                    .setRealm("KIS_REALM")
                    .buildAuthFilter()));

        env.jersey().register(RolesAllowedDynamicFeature.class);
        env.jersey().register(new AuthValueFactoryProvider.Binder<>(KisUser.class));
    }
}