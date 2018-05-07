/**
 * Created by 'Carlos Dávila-Cordero' on 4/25/18.
 */

import React from 'react';
import {Panel, PanelGroup, Table, Label, Navbar, FormGroup, FormControl, Button, Nav, Modal} from 'react-bootstrap';

import '../../assets/style.css'
import {getClusters, getBrokers} from '../../rest/ApiCallsDelegate'

class Sidebar extends React.Component {
    render() {
        return (
            <Modal className='menu-sidebar left' show={this.props.isVisible} onHide={this.props.onHide} autoFocus
                   keyboard>
                <Modal.Header closeButton>
                    <Modal.Title>Clusters</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {this.props.children}
                </Modal.Body>
            </Modal>
        );
    }
}

const ClusterButtonPanel = ({cluster, index, bsStyle, onEnteringClusterPanel}) => {
    let clusterMetadata = cluster['clusterMeta'] || {};
    let clusterTitle = clusterMetadata['title'] || 'N/E';

    return (
        <Panel eventKey={index} bsStyle={bsStyle}>
            <Panel.Heading>
                <Panel.Title toggle>{clusterTitle}</Panel.Title>
            </Panel.Heading>
            <Panel.Collapse onEntering={() => onEnteringClusterPanel(cluster)}>
                <Panel.Body>
                    {<ClusterMetadataTable cluster={cluster}/>}
                </Panel.Body>
            </Panel.Collapse>
        </Panel>
    );
};

const ClusterMetadataTable = ({cluster}) => {
    const ClusterMetadataRow = ({keyName, value}) => {
        return (
            <tr>
                <td>{keyName}</td>
                <td>{value}</td>
            </tr>
        );
    };

    let clusterMeta = cluster['clusterMeta'] || {};

    return (
        <div>
            <Table bordered condensed hover responsive striped>
                <tbody>
                <tr>
                    <th colSpan="2"><b>{'Cluster ' + clusterMeta['clusterTitle'] + ' Metadata'}</b></th>
                </tr>
                <ClusterMetadataRow keyName={<h4><Label bsStyle="success">Id</Label></h4>}
                                    value={<h5>{cluster['clusterId']}</h5>}/>
                <ClusterMetadataRow keyName={<h4><Label bsStyle="success">Title</Label></h4>}
                                    value={<h5>{clusterMeta['title']}</h5>}/>
                <ClusterMetadataRow keyName={<h4><Label bsStyle="success">Secure</Label></h4>}
                                    value={<h5>{clusterMeta['secure'] ? 'true' : 'false'}</h5>}/>
                <ClusterMetadataRow keyName={<h4><Label bsStyle="success">ZK</Label></h4>}
                                    value={<h5>{clusterMeta['zkConnection']}</h5>}/>
                <ClusterMetadataRow keyName={<h4><Label bsStyle="success">Exhibitor</Label></h4>}
                                    value={<h5>{clusterMeta['exhibitor']}</h5>}/>
                <ClusterMetadataRow keyName={<h4><Label bsStyle="success">Bootstrap</Label></h4>}
                                    value={<h5>{clusterMeta['bootstrap']}</h5>}/>
                <ClusterMetadataRow keyName={<h4><Label bsStyle="success">Bootstrap Secure</Label></h4>}
                                    value={<h5>{clusterMeta['bootstrapSecure']}</h5>}/>
                <ClusterMetadataRow keyName={<h4><Label bsStyle="success">Env. Name</Label></h4>}
                                    value={<h5>{clusterMeta['environment']}</h5>}/>
                </tbody>
            </Table>
        </div>
    );
};

export class ClustersPanel extends React.Component {
    constructor(props) {
        super(props);
        this.onSelectClusterPanel = this.onSelectClusterPanel.bind(this);
        this.onChangeFilterClusterTitle = this.onChangeFilterClusterTitle.bind(this);
        this.onClickResetClusterTitleSearch = this.onClickResetClusterTitleSearch.bind(this);
        this.onHideClustersSidePanel = this.onHideClustersSidePanel.bind(this);
        this.refreshClusters = this.refreshClusters.bind(this);

        this.state = {
            filterClusterTitle: ''
        }
    }

    componentDidMount() {
        this.refreshClusters();
    }

    refreshClusters() {
        let fetchClusters = (clusters) => {
            this.props.context.fetchClusters(clusters);
            return clusters;
        };

        let onError = (error) => {
            this.props.context.fetchClusters([]);
            return error;
        };

        getClusters(fetchClusters, onError);
    }

    onChangeFilterClusterTitle(e) {
        this.setState({
            filterClusterTitle: e.target.value
        });
    }

    onClickResetClusterTitleSearch() {
        this.setState({
            filterClusterTitle: ''
        });
    }

    onHideClustersSidePanel() {
        this.props.context.showClustersSidePanel(!this.props.context.appState.isVisibleClustersSidePanel);
        this.forceUpdate();
    }

    onSelectClusterPanel(cluster) {
        if (!cluster.brokers) {
            let onError = (error) => {
                this.props.context.fetchClusterBrokers(cluster, undefined);
                console.error(error);
                return error;
            };

            getBrokers(
                (brokers) => this.props.context.fetchClusterBrokers(cluster, brokers),
                onError,
                cluster.clusterId
            );
        }

        this.props.context.selectCluster(cluster);
    }

    filterByName(cluster, name) {
        let clusterMeta = cluster['clusterMeta'] || {};
        let clusterTile = clusterMeta['title'] || '';

        return clusterTile.toUpperCase().includes(name.toUpperCase());
    }

    createIfClustersExist(clusters) {
        if (clusters.length > 0) {
            return clusters
                .filter(cluster => this.filterByName(cluster, this.state.filterClusterTitle))
                .map((cluster, index) => {
                        return (
                            <ClusterButtonPanel
                                key={index}
                                cluster={cluster}
                                index={index}
                                bsStyle={'primary'}
                                onEnteringClusterPanel={this.onSelectClusterPanel}
                            />
                        );
                    }
                );
        }
        else return null;
    };

    render() {
        let clusters = this.props.context.clustersState.clusters;
        let showSidePanel = this.props.context.appState.isVisibleClustersSidePanel;

        return (
            <div>
                <Sidebar side='left' isVisible={showSidePanel} onHide={this.onHideClustersSidePanel}>
                    <Nav>
                        <Navbar>
                            <Navbar.Header>
                                <Navbar.Toggle />
                            </Navbar.Header>
                            <Navbar.Collapse>
                                <Navbar.Form>
                                    <Button bsStyle="info" onClick={this.refreshClusters}>Refresh</Button>
                                    {' '}
                                    <FormGroup>
                                        <FormControl type="text"
                                                     placeholder="Filter"
                                                     autoFocus
                                                     onChange={this.onChangeFilterClusterTitle}
                                                     value={this.state.filterClusterTitle}/>
                                    </FormGroup>
                                </Navbar.Form>
                            </Navbar.Collapse>
                        </Navbar>
                        <PanelGroup accordion id="accordion-uncontrolled-clusters" defaultActiveKey="1">
                            {this.createIfClustersExist(clusters)}
                        </PanelGroup>
                    </Nav>
                </Sidebar>
            </div>
        );
    }
}