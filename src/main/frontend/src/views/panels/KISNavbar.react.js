/**
 * Created by 'Carlos Dávila-Cordero' on 4/26/18.
 */

import React from 'react';
import {Navbar, Nav, NavItem, Button} from 'react-bootstrap';

export class KISNavbar extends React.Component {

    constructor(props) {
        super(props);
        this.onClickFindCluster = this.onClickFindCluster.bind(this);
    }

    /* This will show the panel if it is not visible */
    onClickFindCluster() {
        if (!this.props.context.appState.isVisibleClustersSidePanel)
            this.props.context.showClustersSidePanel(true);
    }

    render() {
        return (
            <Navbar>
                <Navbar.Header>
                    <Navbar.Brand>
                        <a>{this.props.appName}</a>
                    </Navbar.Brand>
                </Navbar.Header>
                <Nav>
                    <NavItem eventKey={1}>
                        <Button bsStyle="info" onClick={this.onClickFindCluster}>
                            Find cluster
                        </Button>
                    </NavItem>
                </Nav>
            </Navbar>

        );
    }
}