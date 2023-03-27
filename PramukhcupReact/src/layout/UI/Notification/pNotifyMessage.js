import React, {Component} from 'react';
import axios from 'axios';
import Form from 'react-validation/build/form';
import Grid from 'react-bootstrap/lib/Grid';
import Row from 'react-bootstrap/lib/Row';
import Col from 'react-bootstrap/lib/Col';
import Input from '../../../layout/UI/Input';
import { Notification } from 'react-pnotify';

class pNotifyMessage extends Component {

  state={
    type : 'info',
    message : '',
    delaySeconds : 5000
  }
  render(){
    return(<Notification
         type={this.props.type}
         title=''
         text={this.props.message}
         animateIn='lightSpeedIn'
         animateOut='hinge'
         delay={this.props.delaySeconds}
         shadow={false}
         hide={false}
         nonblock={false}
         desktop={true}
       />);
  }

}

export default pNotifyMessage;
