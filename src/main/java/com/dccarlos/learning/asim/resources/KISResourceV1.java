package com.dccarlos.learning.asim.resources;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import com.dccarlos.learning.asim.client.KISDummyProvider.Cluster;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
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

    @GET
    @Path("/brokers")
    @Timed
    public Response getBrokers(@QueryParam("clusterId") Optional<String> clusterId) {
        log.warn("[getBrokers] with clusterId {}", clusterId);

        if (clusterId.isPresent()) {
            List<Broker> brokers = KISDummyProvider.getBrokerList(clusterId.get());
            return Response.ok(brokers, MediaType.APPLICATION_JSON_TYPE).build();
        } else
            return Response.status(Status.NO_CONTENT).build();
    }
}