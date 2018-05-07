/**
 * Created by 'Carlos Dávila-Cordero' on 4/29/18.
 */

import {ReduceStore} from 'flux/utils';
import AppDispatcher from '../dispatchers/AppDispatcher';
import BrokersActionTypes from '../actions/BrokersActionTypes';

const stateCallbacks = (function () {
    function fetchBrokers(state, action) {
        return Object.assign({}, state, {clusters: action.clusters || []});
    }

    return {
        [Symbol.for(BrokersActionTypes.FETCH_BROKERS)]: fetchBrokers
    }
}());


const BrokersStore = class extends ReduceStore {
    constructor() {
        super(AppDispatcher);
        this.getInitialState = this.getInitialState.bind(this);
    }

    getInitialState() {
        return {
            cluster: undefined,
            brokers: []
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

export default new BrokersStore();