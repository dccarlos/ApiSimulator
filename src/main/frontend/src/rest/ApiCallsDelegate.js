/**
 * Created by 'Carlos Dávila-Cordero' on 5/5/18.
 */

export function getClusters(onResponse, onError) {
    fetch('/api/v1/clusters')
        .then(response => response.json())
        .then(onResponse)
        .catch(onError);
}

export function getBrokers(onResponse, onError, idCluster) {
    fetch('/api/v1/brokers?clusterId=' + idCluster)
        .then(response => response.json())
        .then(onResponse)
        .catch(onError);
}