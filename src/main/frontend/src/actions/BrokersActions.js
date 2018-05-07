/**
 * Created by 'Carlos Dávila-Cordero' on 4/29/18.
 */

import BrokersActionTypes from './BrokersActionTypes';
import AppDispatcher from '../dispatchers/AppDispatcher';

const BrokersActions = {
    fetchBrokers(cluster, brokers) {
        AppDispatcher.dispatch({
            type: BrokersActionTypes.FETCH_BROKERS,
            cluster,
            brokers
        });
    }
};

export default BrokersActions;