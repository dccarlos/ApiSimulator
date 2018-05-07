import React from 'react';

import {KISNavbar} from './panels/KISNavbar.react';
import {ClustersPanel} from './panels/ClustersPanel.react'
import {BrokersPanel} from './panels/BrokersPanel.react'

function App(props) {
    return (
        <div>
            <div>
                <KISNavbar appName="Kafka Information Services" context={props}/>
            </div>
            <div>
                <div>
                    <ClustersPanel context={props}/>
                </div>
                <div>
                    <BrokersPanel context={props}/>
                </div>
            </div>
        </div>
    );
}

export default App;