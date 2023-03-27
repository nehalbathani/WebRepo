import React, {Component} from 'react';
import axios from 'axios';
import Form from 'react-validation/build/form';
import Grid from 'react-bootstrap/lib/Grid';
import Row from 'react-bootstrap/lib/Row';
import Col from 'react-bootstrap/lib/Col';
import Input from '../../../layout/UI/Input';
import CommonUtils from '../../../common/CommonUtil';
import './NewPlayer.css';
import { Notification } from 'react-pnotify';
import pNotifyMessage from '../../../layout/UI/Notification/pNotifyMessage';
import ToggleDisplay from 'react-toggle-display';
import  { Redirect } from 'react-router-dom';
import Modal from '../../../layout/UI/Modal/Modal';
import Spinner from '../../../layout/UI/Spinner/Spinner';


class NewPlayer extends Component {

  state={
    addedStatus: false,
    registrationId: 0,
    Player : {
      'firstName' : '',
	    'lastName' : '',
	    'age' : 0,
	    'streetAddr' : '',
	    'city' : '',
	    'state' : '',
	    'zipcode' : 0,
      'contactNo' : 0,
      'emailId' :'',
	    'cricClubId' : '',
	    'playerType' : '',
      'lastYearTeamName' : 'New Player',
	    'paymentMethod' : '',
      'availabilityCode' : 'ALL_DAYS',
      'teamId' : 1,
      'personToPay' :  'Please pay your contribution to Nirvish Patel (908-547-7215) or Sadaf Mansuri (732-318-8004) before il-2018. Please disregard the message if you have already paid.'
    },
    isValid:false,
    show:false,
    showPersonToPay:false,
    showCricClubNotifNewPlayer:false,
    showCricClubNotifExistingPlayer:false,
    showAvailability: false,
    showGuestText: false,
    noftifyText:'',
    NotificationData:{
      type : 'info',
      textNewPlayer : '',
      delayExistingPlayer : 70000,
      textExistingPlayer : '',
      delayNewPlayer : 70000,
      text : '',
      delay : 300000,
      styleName :  'display:none'
    },
    displayModel:false,
    loading: false,
    ModelText: ''
  }

render(){

    const availabilityData = {
    "ALL_DAYS" : "Almost all weekends",
    "ALL_DAYS_ONLY_SAT" : "Almost all weekends - saturday only",
    "ALL_DAYS_ONLY_SUN" : "Almost all weekends - only sunday",
    "THRICE_A_MNTH" : "3 times in a month",
    "THRICE_A_MNTH_ONLY_SAT" : "3 times in a month - only sunday",
    "THRICE_A_MNTH_ONLY_SUN" : "3 times a month - only sunday",
    "TWICE_A_MNTH" : " 2 times in a month",
    "TWICE_A_MNTH_ONLY_SAT" : "2 times in a month - only saturday",
    "TWICE_A_MNTH_ONLY_SUN" : "2 timses a month - only sunday"

  };

  const lastYearTeamData = {
    "New Player" : "New Player",
    "Bravo" : "Bravo",
    "Champions" : "Champions",
    "Warriors" : "Warrios",
    "Rockers" : "Rockers"

  };

  let playerSummary=<div> { this.state.ModelText }</div>;
  if ( this.state.loading ) {
      playerSummary = <Spinner />;
  }
    return (
      <div className='NewPlayer'>
      {this.state.show ?
        <Notification
               type={this.state.NotificationData.type}
               title=''
               text={this.state.NotificationData.text}
               animateIn='lightSpeedIn'
               animateOut='lightSpeedOut'
               delay={this.state.NotificationData.delay}
               shadow={false}
               hide={true}
               nonblock={false}
               desktop={true}
             />: null}
        {this.state.showCricClubNotifExistingPlayer?
               <Notification
                      type={this.state.NotificationData.type}
                      title=''
                      text={this.state.NotificationData.textExistingPlayer}
                      animateIn='lightSpeedIn'
                      animateOut='lightSpeedOut'
                      shadow={false}
                      hide={true}
                      delay={this.state.NotificationData.delayExistingPlayer}
                      nonblock={false}
                      desktop={true}
                    /> : null}
        {this.state.showCricClubNotifNewPlayer ?
                           <Notification
                                  type={this.state.NotificationData.type}
                                  title=''
                                  text={this.state.NotificationData.textNewPlayer}
                                  animateIn='lightSpeedIn'
                                  animateOut='lightSpeedOut'
                                  shadow={false}
                                  delay={this.state.NotificationData.delayNewPlayer}
                                  hide={true}
                                  nonblock={false}
                                  desktop={true}
                                /> : null}

          {this.state.showPersonToPay ?
            <Notification
                 type={this.state.NotificationData.type}
                 title=''
                 text={this.state.Player.personToPay}
                 animateIn='lightSpeedIn'
                 animateOut='lightSpeedOut'
                 delay={this.state.NotificationData.delay}
                 shadow={false}
                 hide={true}
                 nonblock={false}
                 desktop={true}/>
               : null}

               <Modal show={this.state.displayModel} modalClosed={this.modelCancelHandler}>
                   {playerSummary}
               </Modal>
      <Form>
          <Grid>
          <Row>
            <Col xs={18} md={12}>
            <h3>Registration </h3>
            <hr/>
            </Col>
          </Row>
          <Row>
          <Col xs={1} md={1}></Col>
          <Col xs={5} md={4}>
            <Input id='firstName' placeHolderText='First Name' inputtype='text' ctrlname='firstName' changed={this.changeHandler} validations={['required']}/>
          </Col>
          <Col xs={5} md={4}>
            <Input id='lastName' placeHolderText='Last Name' inputtype='text' ctrlname='lastName' changed={this.changeHandler} validations={['required']}/>
          </Col>

          </Row>
          <Row>
          <Col xs={1} md={1}></Col>
          <Col xs={5} md={4}>
            <Input id='contactNo' placeHolderText='Contact#' inputtype='number' changed={this.changeHandler} validations={['required', 'number']}/>
          </Col>
          <Col xs={5} md={4}>
            <Input id='emailId' placeHolderText='Email ID' inputtype='text' ctrlname='emailId' changed={this.changeHandler} validations={['required','email']}/>
          </Col>
          </Row>
          <Row>
          <Col xs={1} md={1}></Col>
          <Col xs={3} md={2}>
            <Input id='age' placeHolderText='Age' inputtype='number' ctrlname='age' changed={this.changeHandler} validations={['required', 'number']}/>
          </Col>
          <Col xs={7} md={6}>
            <Input id='streetAddr' placeHolderText='Street Address:' inputtype='text' changed={this.changeHandler} validations={['required']}/>
          </Col>
          </Row>
          <Row>
          <Col xs={1} md={1}></Col>
          <Col xs={5} md={4}>
            <Input id='city' placeHolderText='City' inputtype='text' changed={this.changeHandler} validations={['required']}/>
          </Col>
            <Col xs={5} md={4}>
            <Input id='zipcode' placeHolderText='Zipcode' inputtype='number' changed={this.changeHandler} validations={['required', 'number']}/>
              </Col>
          </Row>
          <hr/>
          <Row>
          <Col xs={1} md={1}></Col>
          <Col xs={5} md={4}>
            <Row><Input lbltext='Last Year Team' inputtype='label'/></Row>
            <Input id='lastYearTeamName' placeHolderText='Last year team name' lbltext='Last Year Team' inputtype='dropdown' ctrlname='lastYearTeam' changed={this.lastYearTeamChangeHandler}  data={lastYearTeamData}/>
          </Col>
          <Col xs={5} md={4}>
            <Row><Input lbltext='Cricclub Player ID' inputtype='label'/></Row>
            <Input id='cricClubId' placeHolderText='CricClub Player ID' inputtype='text'  changed={this.changeHandler} onFocus={this.onFocusOfCricclub} validations={['required', 'number']} />
            <Row>
              <Col xs={5} md={5}>
                <a href="https://cricclubs.com/AvenelCricketLeague/searchPlayer.do" target="_blank" style={{color:'dodgerblue'}}>GetID</a>
              </Col>
              <Col xs={6} md={6}>
                <a href="https://cricclubs.com/AvenelCricketLeague/registerUser.do?clubId=111"  target="_blank" style={{color:'dodgerblue'}}>Register</a>
              </Col>
             </Row>
          </Col>
          </Row>
          <Row className="top-buffer">
            <Col xs={1} md={1}></Col>
            <Col xs={6} md={5}>
                <Input lbltext='' inputtype='radio' groupname='player_type' val='Regular' radiotxt='Regular-40$' changed={this.playerTypeChangeHandler}/>
                <Input lbltext='' inputtype='radio' groupname='player_type' val='Guest' radiotxt='Guest-10$/game' changed={this.playerTypeChangeHandler}/>
            </Col>
            <Col xs={4} md={3}>
            {this.state.showAvailability ? <div id='availabilityCtrl' >
            <Input lbltext='Availability' inputtype='label'/>
            <Input id='availabilityCode' lbltext='Availability' inputtype='dropdown'ctrlname='availability' changed={this.changeHandler} data={availabilityData}/>
            </div>:null}

            {this.state.showGuestText ? <div id='guestPlayerTxt' >
              <p>
                Register as a guest player if you are not available minimum 2 times a month to play for APL.
              </p>
              <p>
                If regular player is not available then captain will contact  guest players.
                Captain is the final autority to decide the guest player.
              </p>
              <p>
                Per game guest player has to pay 10$ up to 4 games.
                Guest player will not able to play in knockouts.
              </p>
              <p>
                if guest player wants to be a regular player, he will be auctioned between teams who does not have enough players based on availability of existing player.
                Guest player will need to pay remaining money(40-money paid till date).
                Number of games will be counted from 0 for a converted guest player.
                Each player playing in knockout has to play minimum 5 league games excluding rain games.
              </p>
            </div>
            :null}

            </Col>
          </Row>

          <Row>
            <Col xs={1} md={1}></Col>
            <Col xs={5} md={4}>
            </Col>
            <Col xs={5} md={4}>
            </Col>
          </Row>

          <Row>
          <Col xs={1} md={1}></Col>
          <Col xs={11} md={10}>
            <p>By creating an account you agree to our <a href="https://docs.google.com/document/d/1EAGQ_uKRD7mDAaHUo6sr6Bnco7TBJA_c15cnaU5_Nlc/edit?usp=sharing" target="_blank" style={{color:'dodgerblue'}}>Terms & Conditions</a>.</p>
          </Col></Row>
          <Row>
            <Col xs={1} md={2}>
            </Col>
            <Col xs={4} md={3}>
              <button type="button" className="btn btn-success" onClick={this.savePlayer}>Save</button>
            </Col>
            <Col xs={4} md={4}>
            </Col>
          </Row>
          </Grid>
          </Form>
      </div>
);

/*  <Input lbltext='Payment Method' inputtype='label'/>
  <Input lbltext='' inputtype='imgradio' groupname='pay_method' val='BOA' img='BOA' changed={this.payMethodChangeHandler}/>
  <Input lbltext='' inputtype='imgradio' groupname='pay_method' val='Chase' img='Chase' changed={this.payMethodChangeHandler}/>
  <Input lbltext='' inputtype='imgradio' groupname='pay_method' val='PayPal' img='PayPal' changed={this.payMethodChangeHandler}/>
*/
}

modelCancelHandler = () => {
  this.setState( { displayModel: false } );
  this.setState( { loading: false } );
}

  changeHandler = (event) => {
        let inText = event.target.value;
        let inKey = event.target.id;
        let updatedPlayer = this.state.Player;
        updatedPlayer[inKey] = inText;
        this.setState({Player:updatedPlayer});
        this.getPersonToPay();
        //console.log(this.state.Player);
  }

  lastYearTeamChangeHandler = (event) => {
        let inText = event.target.value;
        let inKey = event.target.id;
        let updatedPlayer = this.state.Player;
        updatedPlayer[inKey] = inText;
        updatedPlayer['personToPay']=this.getPersonToPay();
        this.setState({Player:updatedPlayer});
        this.getCricClubId();
  }



  getPersonToPay=()=>{
    console.log('last year team name : '+this.state.Player.lastYearTeamName);
      switch(this.state.Player.lastYearTeamName){
      case 'Bravo':
        return 'Please pay your contribution to Nirvish Patel (908-547-7215) or Sadaf Mansuri (732-318-8004) before 15-April-2018. Please disregard the message if you have already paid.';
      break;
      case 'Champions':
        return 'Please pay your contribution to Ketul Patel (646-712-1497) before 15-April-2018.Please disregard the message if you have already paid.';
      break;
      case 'Warriors':
        return 'Please pay your contribution to Parag (551-666-7954) before 15-April-2018.Please disregard the message if you have already paid.';
      break;
      case 'Rockers':
        return 'Please pay your contribution to Sandip Dhummad (732-512-7705) or Vijay Parikh (732-277-7061) before 15-April-2018.Please disregard the message if you have already paid.';
      break;
      case 'New Player':
        return 'Please pay your contribution to Bhushan Bathani (732-397-6584) before 15-April-2018.Please disregard the message if you have already paid.';
      break;
    }
    //console.log('person to pay : '+this.state.Player.personToPay);
  }


savePlayer=()=>{
  this.setState({displayModel:true});
  this.setState({loading:true});
  if(this.state.Player.personToPay==''){
      this.getPersonToPay();
  }
   let isEmailValid, isNameValid, isZipCodeValid , isContactNoValid = false;
   let errtext='';
  //email validation
  const reg = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    if (this.state.Player.emailId != '' && !reg.test(this.state.Player.emailId)){
      isEmailValid=false;
      errtext='Please enter valid Email ID!';
    }
    else{
      isEmailValid=true;
    }
    //number validation
    const re1 = /^[0-9\b]+$/;
    if (this.state.Player.zipcode != '' && !re1.test(this.state.Player.zipcode)) {
      isZipCodeValid=false;
      errtext='Please enter valid Zipcode!';
    }
    else{
      isZipCodeValid=true;
    }
    const re2 = /^[0-9\b]+$/;
    if (this.state.Player.contactNo != '' && !re2.test(this.state.Player.contactNo)) {
      isContactNoValid=false;
      errtext='Please enter valid Contact No!';
    }
    else{
      isContactNoValid=true;
    }

    //firstname lastname validation
    const re3 = /^[a-zA-Z-,]+(\s{0,1}[a-zA-Z-, ])*$/;
    if (this.state.Player.firstName != '' && !re3.test(this.state.Player.firstName)
        && this.state.Player.lastName != '' && !re3.test(this.state.Player.lastName)) {
      isNameValid=false;
      errtext='Please enter valid Name!';
    }
    else{
      isNameValid=true;
    }

  if(this.state.Player.firstName!='' && this.state.Player.lastName!='' && this.state.Player.age!=''
  && this.state.Player.streetAddr!='' && this.state.Player.city!=''
  && this.state.Player.zipcode!='' && this.state.Player.contactNo!='' && this.state.Player.emailId!=''
  && this.state.Player.cricClubId!='' && this.state.Player.playerType!='' && this.state.Player.lastYearTeamName!=''
  && this.state.Player.availabilityCode!='' && this.state.Player.teamId!=''
  && isEmailValid && isNameValid && isZipCodeValid && isContactNoValid){
     //console.log("validation passed");
     this.state.isValid=true;
   } else {
     this.state.isValid=false;
     if(errtext==''){
       errtext='Please enter all fields to register!';
     }
     //console.log("validation failed");
   }

if(this.state.isValid){
  this.setState({addedStatus:false});
  let playerobj=this.state.Player;
  this.setState({show:false});
  this.setState({showPersonToPay:false});
  axios.post(CommonUtils.getURL()+'/crichost/createPlayer', playerobj )
            .then( response => {
                this.setState( { addedStatus: true } );
                this.setState({noftifyText:response.data.message});
                this.setState({NotificationData:{
                    type : 'success',
                    text : this.state.noftifyText,
                    delay : '15000'
                  }});
                this.setState({loading:false});
                this.setState({ModelText: response.data.message});
                this.setState({show:true});
                setTimeout(function() {
                this.setState({displayModel:false});
                this.props.history.push('/DisplayPlayers'); }.bind(this), 10000);
                //this.setState({showPersonToPay:true});
            } )
            .catch( error => {
                this.setState( { error: true, addedStatus:false } );
                this.setState({show:true});
                this.setState({NotificationData:{
                    type : 'error',
                    text : 'Error! Please contact APL admins(732-397-6584) on private message only!',
                    delay : '10000'
                  }});
                  this.setState({loading:false});
                  this.setState({ModelText: 'Error! Please contact APL admins(732-397-6584) on private message only!'});

            } );
    }
    else{
      this.setState({show:true});
      this.setState({NotificationData:{
          type : 'error',
          text : errtext,
          delay : '10000'
        }});
        this.setState({loading:false});
        this.setState({ModelText:errtext});
    }
    this.setState({loading:false});
    this.setState({displayModel:false});
}

payMethodChangeHandler = (event) => {
      let inText = event.target.value;
      let updatedPlayer = this.state.Player;
      updatedPlayer['paymentMethod'] = inText;
      this.setState({Player:updatedPlayer});
}
playerTypeChangeHandler = (event) => {
      let inText = event.target.value;
      let updatedPlayer = this.state.Player;
      if(inText=='Guest'){
          this.setState({showAvailability:false});
          this.setState({showGuestText:true});
          updatedPlayer['availabilityCode']='Guest';
      }
      else{
        this.setState({showAvailability:true});
        this.setState({showGuestText:false});
      }
      updatedPlayer['playerType'] = inText;
      this.setState({Player:updatedPlayer});

}

   onFocusOfCricclub = ()=>{
     if(this.state.showCricClubNotif==true){
       return; // this means notification is already displayed
     } else {
       this.getCricClubId(); // call cricclub id
     }
   }
   getCricClubId = ()=>{

     if(this.state.Player.firstName!=''&&this.state.Player.lastName!=''&&this.state.Player.lastYearTeamName!=''){
       //console.log("getCricClub");
       //console.log(this.state.Player.lastYearTeamName);
       if(this.state.Player.lastYearTeamName!='New Player'){
           this.setState({showCricClubNotifExistingPlayer:false});
           this.setState({"NotificationData":{"delayExistingPlayer":15000,"delayNewPlayer":0,"textExistingPlayer":"please click on <a href='https://cricclubs.com/AvenelCricketLeague/searchPlayer.do' target='_blank' style={{color:'dodgerblue'}}>GetID</a>. 1) Enter your first name, last name and last year team name on cricclub. 2) Click on your name on search result. Get the player ID and enter in cricclub Id field on registration."}});
           this.setState({showCricClubNotifNewPlayer:false});
           this.setState({showCricClubNotifExistingPlayer:true});
       } else {
           this.setState({showCricClubNotifNewPlayer:false});
           this.setState({"NotificationData":{"delayExistingPlayer":0,"delayNewPlayer":15000,"textNewPlayer":"Please <a href='https://cricclubs.com/AvenelCricketLeague/registerUser.do?clubId=111'  target='_blank' style={{color:'dodgerblue'}}>register</a> on cricclub and enter player ID."}});
           this.setState({showCricClubNotifExistingPlayer:false});
           this.setState({showCricClubNotifNewPlayer:true});
       }

     }
   }


}

export default NewPlayer;
