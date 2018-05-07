/**
 * Created by 'Carlos Dávila-Cordero' on 4/25/18.
 */

import {ReduceStore} from 'flux/utils';
import AppDispatcher from '../dispatchers/AppDispatcher';
import AppActionTypes from '../actions/AppActionTypes';

const stateCallbacks = (function () {
    function showClustersSidePanel(state, action) {
        return Object.assign({}, state, {isVisibleClustersSidePanel: action.flag});
    }

    return {
        [Symbol.for(AppActionTypes.SHW_CLUSTERS_SIDE_MENU)]: showClustersSidePanel
    }
}());


const AppStore = class extends ReduceStore {
    constructor() {
        super(AppDispatcher);
        this.getInitialState = this.getInitialState.bind(this);
    }

    getInitialState() {
        return {
            isVisibleClustersSidePanel: false
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

export default new AppStore();