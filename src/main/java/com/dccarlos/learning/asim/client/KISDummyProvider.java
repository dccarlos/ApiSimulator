package com.dccarlos.learning.asim.client;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class KISDummyProvider {

    private KISDummyProvider() {}

    @Getter
    @Builder
    @ToString
    @EqualsAndHashCode
    @JsonDeserialize(builder = Cluster.ClusterBuilder.class)
    public static final class Cluster {
        private final String clusterId;
        private final ClusterMeta clusterMeta;
        @JsonPOJOBuilder(withPrefix = "")
        public static final class ClusterBuilder {}
    }

    @Getter
    @Builder
    @ToString
    @EqualsAndHashCode
    @JsonDeserialize(builder = ClusterMeta.ClusterMetaBuilder.class)
    public static final class ClusterMeta {
        private final String title;
        private final Boolean secure;
        private final String zkConnection;
        private final String exhibitor;
        private final String bootstrap;
        private final String bootstrapSecure;
        private final String environment;
        @JsonPOJOBuilder(withPrefix = "")
        public static final class ClusterMetaBuilder {}
    }

    @Getter
    @Builder
    @ToString
    @EqualsAndHashCode
    @JsonDeserialize(builder = Broker.BrokerBuilder.class)
    public static final class Broker {
        private final String brokerId;
        private final BrokerMeta brokerMeta;
        @JsonPOJOBuilder(withPrefix = "")
        public static final class BrokerBuilder {}
    }

    @Getter
    @Builder
    @ToString
    @EqualsAndHashCode
    @JsonDeserialize(builder = BrokerMeta.BrokerMetaBuilder.class)
    public static final class BrokerMeta {
        private String hostname;
        private String ipAddress;
        private String version;
        private String brokerState;
        private Boolean isController;
        private String clusterId;

        @JsonPOJOBuilder(withPrefix = "")
        public static final class BrokerMetaBuilder {}
    }
    
    public static final List<Cluster> getClusterList() {
        return IntStream.range(0, clusterTitles.length)
                .mapToObj(id -> createCluster(id + 1, clusterTitles[id]))
                .collect(Collectors.toList());
    }
    
    public static final List<Broker> getBrokerList(String clusterId) {
        Random r = new Random();
        final int min = 5;
        final int max = 15;
        final int brokers = (r.nextInt((max - min) + 1) + min);

        return IntStream
                .range(0, brokers)
                .mapToObj(id -> createBroker(clusterId, id + 1))
                .collect(Collectors.toList());
    }
    
    public static final Cluster createCluster(int id, String name) {
        final String nameForUrl = name.toLowerCase().replace("_", "-");
        return Cluster.builder()
                .clusterId("" + id)
                .clusterMeta(ClusterMeta.builder()
                        .title(name)
                        .secure(true)
                        .zkConnection(String.format("http://%s-zk", nameForUrl))
                        .exhibitor(String.format("http://%s-exhibitor", nameForUrl))
                        .bootstrap(String.format("http://%s-kafka", nameForUrl))
                        .bootstrapSecure(String.format("https://%s-kafka", nameForUrl))
                        .environment(String.format("CLUSTER_%s_ENV", nameForUrl))
                        .build())
                .build();
    }
    
    public static final Broker createBroker(String clusterId, int brokerNumber) {
        Random random = new Random();
        String name = String.format("broker-%d", brokerNumber);
        return Broker.builder()
            .brokerId(String.format("%s|%s", clusterId, name))
            .brokerMeta(BrokerMeta.builder()
                    .hostname(String.format("%s-kafka", name))
                    .ipAddress(getRandomIp())
                    .version(getRandomVersionInRange(1, 10))
                    .brokerState(brokerStates[random.nextInt(brokerStates.length)])
                    .isController(random.nextBoolean())
                    .clusterId(clusterId)
                    .build())
            .build();
    }
    
    public static final String[] clusterTitles = new String[]{
            "AUSTIN-SECURE_DEV", 
            "AUSTIN_DEV", 
            "AUSTIN_TEST", 
            "AWS-EU-WEST-1_DEV", 
            "AWS-EU-WEST-1_TEST",
            "AWS-US-EAST-1_DEV", 
            "AWS-US-EAST-1_EDAP2-TEST", 
            "AWS-US-EAST-1_TEST", 
            "LEGACY_TEST", 
            "MOT-EU-WEST-1_DEV", 
            "MOT-EU-WEST-1_TEST",
            "MOT-US-EAST-1_DEV", 
            "MOT-US-EAST-1_TEST", 
            "MOT-US-WEST-2_TEST", 
            "US-East-EDAP-2_TEST", 
            "US-East-EDAP_TEST",
            "AWS-EU-WEST-1_STAGE", 
            "AWS-US-EAST-1_EDAP2-STAGE", 
            "AWS-US-EAST-1_STAGE", 
            "MOT-AP-SOUTHEAST-1_STAGE", 
            "MOT-EU-WEST-1_STAGE", 
            "MOT-US-EAST-1_STAGE",
            "US-East-EDAP_STAGE"
    };
    
    public static final String[] brokerStates = new String[]{
            "NOT_RUNNING",
            "STARTING",
            "RECOVERING",
            "RUNNING",
            "PENDING_SHUTDOWN",
            "SHUTTING_DOWN"
    };
        
    public static final String getRandomIp() {
        Random r = new Random();
        return String.format("%d.%d.%d.%d", r.nextInt(256), r.nextInt(256), r.nextInt(256), r.nextInt(256));
    }

    public static String getRandomVersionInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return String.format("v%d.%d", (r.nextInt((max - min) + 1) + min), (r.nextInt((max - min) + 1) + min));
    }
}