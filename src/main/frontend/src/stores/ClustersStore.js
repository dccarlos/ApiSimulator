/**
 * Created by 'Carlos Dávila-Cordero' on 4/25/18.
 */

import {ReduceStore} from 'flux/utils';
import AppDispatcher from '../dispatchers/AppDispatcher';
import ClustersActionTypes from '../actions/ClustersActionTypes';

const stateCallbacks = (function () {
    function fetchClusters(state, action) {
        return Object.assign({}, state, {clusters: action.clusters || []});
    }

    function fetchClusterBrokers(state, action) {
        var clusters = state.clusters;
        clusters.find(e => (e.clusterId === action.cluster.clusterId))['brokers'] = action.brokers;

        return Object.assign({}, state, {clusters: clusters});
    }

    function selectCluster(state, action) {
        return Object.assign({}, state, {selectedCluster: action.cluster});
    }

    return {
        [Symbol.for(ClustersActionTypes.FETCH_CLUSTERS)]: fetchClusters,
        [Symbol.for(ClustersActionTypes.FETCH_CLUSTER_BROKERS)]: fetchClusterBrokers,
        [Symbol.for(ClustersActionTypes.SELECT_CLUSTER)]: selectCluster
    }
}());


const ClustersStore = class extends ReduceStore {
    constructor() {
        super(AppDispatcher);
        this.getInitialState = this.getInitialState.bind(this);
    }

    getInitialState() {
        return {
            clusters: [],
            selectedCluster: undefined
        };
    }

    reduce(state, action) {
        if (stateCallbacks[Symbol.for(action.type)]) {
            return stateCallbacks[Symbol.for(action.type)](state, action);
        }
        else {
            return state;
        }
    }
};

export default new ClustersStore();