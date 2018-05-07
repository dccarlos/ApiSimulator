/**
 * Created by 'Carlos Dávila-Cordero' on 4/25/18.
 */

import {Container} from 'flux/utils';

import App from '../views/App';

import AppStore from '../stores/AppStore';
import AppActions from '../actions/AppActions';
import ClustersStore from '../stores/ClustersStore';
import ClustersActions from '../actions/ClustersActions';
import BrokersStore from '../stores/BrokersStore';
import BrokersActions from '../actions/BrokersActions';

function getStores() {
    return [
        AppStore,
        ClustersStore,
        BrokersStore
    ];
}

function getState() {
    return {
        // ==================== State ====================
        // ------- App --------
        appState: AppStore.getState(),

        // ----- Clusters -----
        clustersState: ClustersStore.getState(),

        // ----- Brokers ------
        brokersState: BrokersStore.getState(),
        // =================== Actions ===================
        // ------- App --------
        showClustersSidePanel: AppActions.showClustersSidePanel,

        // ----- Clusters -----
        fetchClusters: ClustersActions.fetchClusters,
        fetchClusterBrokers: ClustersActions.fetchClusterBrokers,
        selectCluster: ClustersActions.selectCluster,

        // ----- Brokers ------
        fetchBrokers: BrokersActions.fetchBrokers
    };
}

export default Container.createFunctional(App, getStores, getState);