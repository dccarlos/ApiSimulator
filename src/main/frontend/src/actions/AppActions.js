/**
 * Created by 'Carlos Dávila-Cordero' on 4/25/18.
 */

import AppActionTypes from './AppActionTypes';
import AppDispatcher from '../dispatchers/AppDispatcher';

const AppActions = {
    showClustersSidePanel(flag) {
        AppDispatcher.dispatch({
            type: AppActionTypes.SHW_CLUSTERS_SIDE_MENU,
            flag
        });
    }
};

export default AppActions;