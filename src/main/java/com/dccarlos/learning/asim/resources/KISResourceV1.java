package com.dccarlos.learning.asim.resources;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.codahale.metrics.annotation.Timed;
import com.dccarlos.learning.asim.client.KISDummyProvider;
import com.dccarlos.learning.asim.client.KISDummyProvider.Broker;
import com.dccarlos.learning.asim.client.KISDummyProvider.BrokerConfig;
import com.dccarlos.learning.asim.client.KISDummyProvider.Cluster;
import com.dccarlos.learning.asim.client.KISDummyProvider.Operation;
import com.dccarlos.learning.asim.client.KISDummyProvider.ZkResponse;
import com.dccarlos.learning.asim.resources.security.KisUser;
import com.fasterxml.jackson.databind.JsonNode;

import io.dropwizard.auth.Auth;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@PermitAll
@Path("/v1") /* Remember config in YAML: 'rootPath: /api/' */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class KISResourceV1 {
    @GET
    @Path("/clusters")
    @Timed
    public Response getClusters() {
        log.warn("[getClusters] is being called");
        return Response.ok(KISDummyProvider.getClusterList(), MediaType.APPLICATION_JSON_TYPE).build();
    }

    @GET
    @Path("/clusters/{id}")
    @Timed
    public Response getCluster(@PathParam("id") String id) {
        log.warn("[getCluster] with id {}", id);

        Predicate<Cluster> isTheSameId = cluster -> cluster.getClusterId().equals(id);
        Optional<Cluster> cluster = KISDummyProvider.getClusterList().stream().filter(isTheSameId).findAny();

        if (cluster.isPresent())
            return Response.ok(cluster.get(), MediaType.APPLICATION_JSON_TYPE).build();
        else
            return Response.status(Status.NOT_FOUND).build();
    }

    @POST
    @Path("/admin/operations")
    @Timed
    public Response executeOperation(@Auth KisUser user, @NotNull JsonNode operation) {
        log.warn("[executeOperation] is being called {}", operation);
        return Response.ok(KISDummyProvider.getRandomResponse(), MediaType.APPLICATION_JSON_TYPE).build();
    }

    @GET
    @Path("/admin/operations")
    @Timed
    public Response getOperations() {
        log.warn("[getOperations] is being called");
        return Response.ok(KISDummyProvider.getOperations(), MediaType.APPLICATION_JSON_TYPE).build();
    }

    @GET
    @Path("/admin/operations/{id}")
    @Timed
    public Response getOperationById(@PathParam("id") String id) {
        log.warn("[getOperationById] is being called {}", id);

        Predicate<Operation> isTheSameId = cluster -> cluster.getOperationId().toString().equals(id);
        Optional<Operation> operation = KISDummyProvider.getOperations().stream().filter(isTheSameId).findAny();

        if (operation.isPresent()) {
            return Response.ok(operation.get(), MediaType.APPLICATION_JSON_TYPE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/brokers")
    @Timed
    public Response getBrokers(@Auth KisUser user, @QueryParam("clusterId") Optional<String> clusterId) {
        log.warn("[getBrokers] with clusterId {}", clusterId);

        if (clusterId.isPresent()) {
            List<Broker> brokers = KISDummyProvider.getBrokerList(clusterId.get());
            return Response.ok(brokers, MediaType.APPLICATION_JSON_TYPE).build();
        } else
            return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    @Path("/mirrormakers")
    @Timed
    public Response getMirrorMakers(@Auth KisUser user, @QueryParam("srcCluster") Optional<String> srcCluster) {
        log.warn("[getMirrorMakers] requested by {}", user);

        if (srcCluster.isPresent()) {
            return Response.ok(KISDummyProvider.getListOfMirrorMakers().stream().filter(mm -> mm.getMirrorMakerMeta().getSrcCluster().equals(srcCluster.get()))
                    .collect(Collectors.toList()), MediaType.APPLICATION_JSON_TYPE).build();
        } else {
            return Response.ok(KISDummyProvider.getListOfMirrorMakers(), MediaType.APPLICATION_JSON_TYPE).build();
        }

    }

    @GET
    @Path("/admin/configuration")
    @Timed
    public Response getAdminConfiguration(
            @QueryParam("fromFile") Optional<Boolean> fromFile,
            @QueryParam("brokerId") String brokerId,
            @QueryParam("clusterId") String clusterId
            ) {
        log.warn("[getAdminConfiguration] with brokerId {} and clusterId {} (is from file?: {})", brokerId, clusterId, fromFile.orElse(false));

        Optional<BrokerConfig> brokerConfig = Optional.ofNullable(KISDummyProvider.getBrokerConfigOf(brokerId != null ? clusterId : null));

        if (brokerConfig.isPresent())
            return Response.ok(brokerConfig.get(), MediaType.APPLICATION_JSON_TYPE).build();
        else
            return Response.status(Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/admin/configuration")
    @Timed
    public Response updateAdminConfiguration(
            @QueryParam("fromFile") Optional<Boolean> fromFile,
            @QueryParam("brokerId") String brokerId,
            @QueryParam("clusterId") String clusterId,
            @NotNull JsonNode operation
            ) {
        log.warn("[updateAdminConfiguration] with brokerId {} and clusterId {} (is from file?: {})", brokerId, clusterId, fromFile.orElse(false));
        log.warn("Document: {}", operation);

        Optional<BrokerConfig> brokerConfig = Optional.ofNullable(KISDummyProvider.getBrokerConfigOf(brokerId != null ? clusterId : null));

        if (brokerConfig.isPresent()) {
            return Response.accepted().build();
        } else
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("/zk")
    @Timed
    public Response getZkBrokers(@Auth KisUser user, @QueryParam("clusterId") Optional<String> clusterId) {
        log.warn("[getBrokers] with clusterId {}", clusterId);

        if (clusterId.isPresent()) {
            ZkResponse zkResponse = KISDummyProvider.getZkBrokers(clusterId.get());
            return Response.ok(zkResponse, MediaType.APPLICATION_JSON_TYPE).build();
        } else
            return Response.status(Status.NO_CONTENT).build();
    }
}