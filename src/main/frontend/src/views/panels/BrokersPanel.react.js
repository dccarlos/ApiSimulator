/**
 * Created by 'Carlos Dávila-Cordero' on 4/29/18.
 */

import React from 'react';
import {Table, Panel} from 'react-bootstrap';

export class BrokersPanel extends React.Component {

    getBrokers(cluster) {
        let brokers = cluster.brokers;
        return brokers.map((broker, index) => {
                let meta = broker['brokerMeta'] || {};
                return (
                    <tr key={index}>
                        <td>{broker.brokerId}</td>
                        <td>{meta.hostname}</td>
                        <td>{meta.ipAddress}</td>
                        <td>{meta.version}</td>
                        <td>{meta.brokerState}</td>
                        <td>{meta.isController ? 'true' : 'false'}</td>
                    </tr>
                );
            }
        );
    }

    render() {
        let cluster = this.props.context.clustersState.selectedCluster;

        if (cluster && cluster.brokers && cluster.brokers.length > 0) {
            let brokers = this.getBrokers(cluster);
            let clusterMeta = cluster['clusterMeta'] || {};
            let clusterTile = clusterMeta['title'] || '';
            return (
                <div>
                    <Panel bsStyle="info">
                        <Panel.Heading>
                            <Panel.Title componentClass="h3">{clusterTile + ' Brokers'}</Panel.Title>
                        </Panel.Heading>
                        <Panel.Body>
                            <Table bordered condensed hover responsive striped>
                                <tbody>
                                <tr>
                                    <th>Id</th>
                                    <th>Hostname</th>
                                    <th>IP address</th>
                                    <th>Version</th>
                                    <th>State</th>
                                    <th>Controller</th>
                                </tr>
                                {brokers}
                                </tbody>
                            </Table>
                        </Panel.Body>
                    </Panel>
                </div>
            );
        }
        else return null;
    }
}