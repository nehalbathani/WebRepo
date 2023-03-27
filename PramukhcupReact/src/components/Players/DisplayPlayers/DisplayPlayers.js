import React, {Component} from 'react';
import axios from 'axios';
import ToggleDisplay from 'react-toggle-display';
import Form from 'react-validation/build/form';
import Grid from 'react-bootstrap/lib/Grid';
import Row from 'react-bootstrap/lib/Row';
import Col from 'react-bootstrap/lib/Col';
import CommonUtils from '../../../common/CommonUtil';
import { Notification } from 'react-pnotify';
import 'react-bootstrap-table/dist/react-bootstrap-table-all.min.css';
var ReactBsTable  = require('react-bootstrap-table');
var BootstrapTable = ReactBsTable.BootstrapTable;
var TableHeaderColumn = ReactBsTable.TableHeaderColumn;



class DisplayPlayers extends Component {
  state={
    Players: [{
      playerId: 1,
      firstName: "BHUSHAN",
      lastName: "BATHANI",
      age: 34,
      emailId:null,
      contactNo:null,
      streetAddr: "18 Berkley blvd",
      city:"Iselin",
      state:"",
      zipcode:8830,
      availabilityCode:"TWICE_A_MNTH",
      cricClubId:"763618",
      lastYearTeamName:null,
      paymentMethod:"",
      playerType:"Regular",
      teamId:1
    }],
    show:false,
    NotificationData:{
      type : 'info',
      text : '',
      delay : 3000,
      styleName :  'display:none'
    }
  };

componentDidMount(){


  axios.post(CommonUtils.getURL()+'/crichost/listPlayer',null,{'Content-Type':'application/json'})
            .then( response => {
                console.log('success');
                this.setState({Players:response.data});
            } )
            .catch( error => {
                this.setState( { error: true } );
                console.log(error);
                this.setState({show:true});
                this.setState({NotificationData:{
                    type : 'error',
                    text : 'Error! Please contact APL admins(732-397-6584) on private message only!',
                    delay : '10000'
                  }});

            } );

}

cricClubLinkFormatter(cell, row) {   // String example
  let cricClubURL='https://cricclubs.com/AvenelCricketLeague/viewPlayer.do?playerId='+cell+'&clubId=111';
  return <a href={cricClubURL}  target="_blank">view</a>;
}
  render(){
    return (
      <div className="DisplayPlayers" >
      <Grid>
      <Row>
        <Col xs={18} md={12}>
        <h3>Player Details  </h3>
        <hr/>
        </Col>
      </Row>
      <Row>
      <Col xs={0} md={0}></Col>
      <Col xs={18} md={12}>

        <BootstrapTable data={this.state.Players} striped hover align='center' width='100%' height='500' options={ { noDataText: 'No Player data in the record!' } }>
            <TableHeaderColumn width='50' isKey={true} dataField='playerId'>ID</TableHeaderColumn>
            <TableHeaderColumn width='110' dataField='firstName'>First Name</TableHeaderColumn>
            <TableHeaderColumn width='110' dataField='lastName'>Last Name</TableHeaderColumn>
            <TableHeaderColumn width='90' dataField='cricClubId' dataFormat={ this.cricClubLinkFormatter }>Cricclub Profile</TableHeaderColumn>
            <TableHeaderColumn width='100' dataField='playerType'>Player Type</TableHeaderColumn>
            <TableHeaderColumn width='150' dataField='availabilityCode'>Availability</TableHeaderColumn>
            <TableHeaderColumn width='60' dataField='paymentMethod'>Paid</TableHeaderColumn>
            <TableHeaderColumn width='100' dataField='teamName'>TeamName</TableHeaderColumn>
            <TableHeaderColumn width='120' dataField='lastYearTeamName'>Last Year Team</TableHeaderColumn>
            <TableHeaderColumn width='50' dataField='age'>Age</TableHeaderColumn>
            <TableHeaderColumn width='120' dataField='streetAddr'>Street Address</TableHeaderColumn>
            <TableHeaderColumn width='80' dataField='city'>City</TableHeaderColumn>
            <TableHeaderColumn width='150' dataField='emailId'>Email Id</TableHeaderColumn>
            <TableHeaderColumn width='100' dataField='contactNo'>Contact No</TableHeaderColumn>
        </BootstrapTable>
      </Col>
      </Row>
      </Grid>
      </div>
    );
  }
}

export default DisplayPlayers;
