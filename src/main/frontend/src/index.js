import React from 'react';
import ReactDOM from 'react-dom';
import "bootstrap/dist/css/bootstrap.css";

import {BrowserRouter as Router, Route} from 'react-router-dom';

import AppContainer from './containers/AppContainer';

/* import registerServiceWorker from './utils/registerServiceWorker'; */

ReactDOM.render(
    <Router>
        <div>
            <Route exact path="/" component={AppContainer}/>
        </div>
    </Router>,
    document.getElementById('root'));

/* registerServiceWorker(); */