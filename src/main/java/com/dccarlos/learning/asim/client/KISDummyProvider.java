package com.dccarlos.learning.asim.client;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.joda.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.google.common.collect.ImmutableMap;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
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
    @JsonDeserialize(builder = ZkResponse.ZkResponseBuilder.class)
    public static final class ZkResponse {
        private final String clusterId;
        @Singular private final List<ZkServer> servers;
        @JsonPOJOBuilder(withPrefix = "")
        public static final class ZkResponseBuilder {}
    }
    
    @Getter
    @Builder
    @ToString
    @EqualsAndHashCode
    @JsonDeserialize(builder = ZkServer.ZkServerBuilder.class)
    public static final class ZkServer {
        private final String hostname;
        private final Integer code;
        private final String description;
        @JsonProperty(value = "isLeader")
        private final boolean isLeader;
        private final String instanceStateType;

        @JsonPOJOBuilder(withPrefix = "")
        public static final class ZkServerBuilder {}
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
        private Boolean staleConfigs;
        private String rack;
        private Long latestStartTime;
        private Long latestUpdate;

        @JsonPOJOBuilder(withPrefix = "")
        public static final class BrokerMetaBuilder {}
    }
    
    @Getter
    @Builder
    @ToString
    @EqualsAndHashCode
    @JsonDeserialize(builder = ExecuteOperationResponse.ExecuteOperationResponseBuilder.class)
    public static final class ExecuteOperationResponse {
        private final Integer operationId;
        @JsonPOJOBuilder(withPrefix = "")
        public static final class ExecuteOperationResponseBuilder {}
    }
    
    @Getter
    @Builder(toBuilder = true)
    @ToString
    @EqualsAndHashCode
    @JsonDeserialize(builder = Operation.OperationBuilder.class)
    public static final class Operation {
        private final OperationDefinition operationDefinition;
        private final Integer operationId;
        private final String status;
        private final Long startTime;
        private final Long finishTime;
        private final List<Integer> childOperations;
        @JsonPOJOBuilder(withPrefix = "")
        public static final class OperationBuilder {}
    }
    
    @Getter
    @Builder(toBuilder = true)
    @ToString
    @EqualsAndHashCode
    @JsonDeserialize(builder = OperationSubject.OperationSubjectBuilder.class)
    public static final class OperationSubject {
        private final String target;
        private final String type;
        @JsonPOJOBuilder(withPrefix = "")
        public static final class OperationSubjectBuilder {}
    }
    
    @Getter
    @Builder(toBuilder = true)
    @ToString
    @EqualsAndHashCode
    @JsonDeserialize(builder = OperationDefinition.OperationDefinitionBuilder.class)
    public static final class OperationDefinition {
        private final String type;
        private final OperationSubject target;
        @Singular private final Map<String, String> properties;
        @JsonPOJOBuilder(withPrefix = "")
        public static final class OperationDefinitionBuilder {}
    }
    
    @Getter
    @Builder(toBuilder = true)
    @ToString
    @EqualsAndHashCode
    @JsonDeserialize(builder = BrokerConfig.BrokerConfigBuilder.class)
    public static final class BrokerConfig {
        private final String brokerId;
        @Singular
        private final Map<String, String> properties;
        @JsonPOJOBuilder(withPrefix = "")
        public static final class BrokerConfigBuilder {}
    }
    
    @Getter
    @Builder(toBuilder = true)
    @ToString
    @EqualsAndHashCode
    @JsonDeserialize(builder = MirrorMaker.MirrorMakerBuilder.class)
    public static final class MirrorMaker {
        private final String motAppName;
        private final MirrorMakerMeta mirrorMakerMeta;

        public static final class MirrorMakerBuilder {}
    }
    
    @Getter
    @Builder(toBuilder = true)
    @ToString
    @EqualsAndHashCode
    @JsonDeserialize(builder = MirrorMakerMeta.MirrorMakerMetaBuilder.class)
    public static final class MirrorMakerMeta {
        private final String srcCluster;
        private final String destCluster;
        private final String deployedRegion;
        private final String version;
        @Singular private final List<String> topics;
        
        public static final class MirrorMakerMetaBuilder {}
    }
    
    public static final BrokerConfig getBrokerConfigOf(String id) {
        return BrokerConfig.builder()
                .brokerId(id)
                .properties(brokerProperties
                        .entrySet()
                        .stream()
                        .map(KISDummyProvider::toRandomValueForBrokerPropertiesEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .build();
    }
    
    public static final Map.Entry<String, String> toRandomValueForBrokerPropertiesEntry(Map.Entry<String, String> e) {
        Random r = new Random();
        int min = 100;
        int max = 10000;

        String newValue;

        switch (e.getKey()) {
            case "log.cleaner.min.compaction.lag.ms" :
            case "offsets.topic.num.partitions" :
            case "log.flush.interval.messages" :
            case "controller.socket.timeout.ms" :
            case "min.insync.replicas" :
            case "num.recovery.threads.per.data.dir" :
                newValue = String.format("%d", (r.nextInt((max - min) + 1) + min));
                break;
            default :
                newValue = e.getValue();
        }

        return new AbstractMap.SimpleEntry<String, String>(e.getKey(), newValue);
    }
    
    public static ExecuteOperationResponse getRandomResponse() {
        Random r = new Random();
        int max = 1000;
        int min = 100;
        
        return ExecuteOperationResponse.builder().operationId((r.nextInt((max - min) + 1) + min)).build();
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
    
    public static final ZkResponse getZkBrokers(String clusterId) {
        Random r = new Random();
        final int min = 3;
        final int max = 5;
        final int brokers = (r.nextInt((max - min) + 1) + min);
        
        return ZkResponse.builder()
                .clusterId(clusterId)
                .servers(IntStream
                        .range(0, brokers)
                        .mapToObj(id -> createZkBroker(clusterId))
                        .collect(Collectors.toList()))
                .build();
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
                    .staleConfigs(random.nextBoolean())
                    .rack(String.format("rack-of-broker-%d", brokerNumber))
                    .latestStartTime(Instant.now().getMillis())
                    .latestUpdate(Instant.now().getMillis())
                    .build())
            .build();
    }
    
    public static final ZkServer createZkBroker(String clusterId) {
        Random random = new Random();
                
        return ZkServer.builder()
            .hostname(getRandomIp())
            .code(random.nextInt())
            .description("serving")
            .isLeader(random.nextBoolean())
            .instanceStateType("SERVING")
            .build();
    }
    
    public static final List<Operation> getOperations() {
        AtomicInteger counter = new AtomicInteger(0);
        
        List<Operation> operations = IntStream.range(0, clusterTitles.length)
                .mapToObj(id -> createOperations(id + 1))
                .flatMap(List::stream)
                .map(Operation::toBuilder)
                .map(builder -> addIdToOperation(counter, builder))
                .collect(Collectors.toList());
        
        List<Operation> withChildren = operations.stream()
                .map(p -> createChildOperations(p, operations))
                .collect(Collectors.toList());
        
        return withChildren;
    }
    
    public static final Operation createChildOperations(Operation parent, List<Operation> operations) {        
        return parent.toBuilder()
                .childOperations(operations.stream()
                        .filter(op -> isChildOf(parent, op))
                        .map(op -> op.getOperationId())
                        .collect(Collectors.toList()))
                .build();
    }
    
    public static final boolean isChildOf(Operation parent, Operation child) {
        OperationSubject parentTarget = parent.getOperationDefinition().getTarget();
        OperationSubject childTarget = child.getOperationDefinition().getTarget();

        return (childTarget.getTarget().contains(":") && childTarget.getTarget().split(":")[0].equals(parentTarget.getTarget()));
    }
    
    public static Operation addIdToOperation(AtomicInteger counter, Operation.OperationBuilder builder) {
        return builder.operationId(counter.incrementAndGet()).build();
    }
    
    public static final List<Operation> createOperations(int targetId) {
        Random r = new Random();
        final int min = 1;
        final int max = 4;
        final int operations = (r.nextInt((max - min) + 1) + min);

        return IntStream
                .range(0, operations)
                .mapToObj(id -> createOperation(targetId, String.format("broker-%d", (id + 1))))
                .collect(Collectors.toList());
    }

    public static final Operation createOperation(int targetId, String name) {
        Random random = new Random();
        
        boolean isCluster = random.nextBoolean();
        
        final String target = isCluster ? "" + targetId : targetId + ":" + String.format("%s|%s", targetId, name);
        final String type = isCluster ? "CLUSTER" : "BROKER";
        
        return Operation.builder()
                .operationDefinition(OperationDefinition.builder()
                        .type(operationTypes[random.nextInt(operationTypes.length)])
                        .target(OperationSubject.builder()
                                .target(target)
                                .type(type)
                                .build())
                        .property("foo", "bar")
                        .build())
                .operationId(targetId)
                .status(statuses[random.nextInt(statuses.length)])
                .startTime(Instant.now().getMillis())
                .finishTime(Instant.now().getMillis())
                .build();
    }
    
    public static final List<MirrorMaker> getListOfMirrorMakers() {
        // Random r = new Random();
        // final int min = 1;
        final int max = clusterTitles.length;
        // final int instances = (r.nextInt((max - min) + 1) + min);

        return IntStream.range(0, max)
                .mapToObj(id -> createMirrorMakerInstance(max, id))
                .collect(Collectors.toList());
    }
    
    public static final MirrorMaker createMirrorMakerInstance(int numberOfClusters, int id) {
        Random r = new Random();
        return MirrorMaker.builder()
                .motAppName(String.format("mirror-maker-mot-instance-%d", id))
                .mirrorMakerMeta(MirrorMakerMeta.builder()
                        .deployedRegion(clusterTitles[r.nextInt(numberOfClusters)].split("_")[0])
                        .destCluster(String.format("%d", (r.nextInt(numberOfClusters) + 1)))
                        .srcCluster(String.format("%d", (id + 1)))
                        .version(getRandomVersion())
                        .topic(String.format("topic-%d-test", (r.nextInt(numberOfClusters) + 1)))
//                        .topic(String.format("topic-%d-test", (r.nextInt(numberOfClusters) + 1)))
//                        .topic(String.format("topic-%d-test", (r.nextInt(numberOfClusters) + 1)))
                        .build())
                .build();
    }
    
    public static final String[] operationTypes = new String[]{
            "START",
            "STOP",
            "RESTART",
            "UPGRADE"
    };
    
    public static final String[] statuses = new String[]{
            "PENDING",
            "RUNNING",
            "PAUSED",
            "COMPLETE",
            "FAILED",
            "SKIPPED",
    };
    
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
        
    public static final Map<String, String> brokerProperties = ImmutableMap.<String, String>builder()
            .put("log.cleaner.min.compaction.lag.ms", "0")
            .put("offsets.topic.num.partitions", "50")
            .put("log.flush.interval.messages", "9223372036854775807")
            .put("controller.socket.timeout.ms", "30000")
            .put("principal.builder.class", "org.apache.kafka.common.security.auth.DefaultPrincipalBuilder")
            .put("min.insync.replicas", "1")
            .put("ssl.keystore.type", "JKS")
            .put("num.recovery.threads.per.data.dir", "2")
            .build();
        
    public static final String getRandomIp() {
        Random r = new Random();
        return String.format("%d.%d.%d.%d", r.nextInt(256), r.nextInt(256), r.nextInt(256), r.nextInt(256));
    }
    
    public static final String getRandomVersion() {
        Random r = new Random();
        return String.format("%d.%d", r.nextInt(256), r.nextInt(256));
    }

    public static String getRandomVersionInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return String.format("v%d.%d", (r.nextInt((max - min) + 1) + min), (r.nextInt((max - min) + 1) + min));
    }
}