/**
 * Created by 'Carlos Dávila-Cordero' on 4/25/18.
 */

import ClustersActionTypes from './ClustersActionTypes';
import AppDispatcher from '../dispatchers/AppDispatcher';

const ClustersActions = {
    fetchClusters(clusters) {
        AppDispatcher.dispatch({
            type: ClustersActionTypes.FETCH_CLUSTERS,
            clusters
        });
    },

    fetchClusterBrokers(cluster, brokers) {
        AppDispatcher.dispatch({
            type: ClustersActionTypes.FETCH_CLUSTER_BROKERS,
            cluster,
            brokers
        });
    },

    selectCluster(cluster) {
        AppDispatcher.dispatch({
            type: ClustersActionTypes.SELECT_CLUSTER,
            cluster
        });
    },
};

export default ClustersActions;