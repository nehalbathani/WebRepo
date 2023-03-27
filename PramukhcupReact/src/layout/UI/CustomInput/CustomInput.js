import React, {Component} from 'react';
import './CustomInput.css';
import { Notification } from 'react-pnotify';
import pNotifyMessage from '../../../layout/UI/Notification/pNotifyMessage';
import ToggleDisplay from 'react-toggle-display';

class CustomInput extends React.Component{
  state={
    isValid:true,
    errorMsg:'',
    show:false,
    notifyText:''
  }

   validate = (event,validations) => {
    // console.log(event.target);
     this.setState({show:false});
     let isInvalid=false;
     if(validations){
     for (var i=0; i<validations.length;i++) {
       switch(validations[i]){
         case 'required':
         if (event.target.value==null || event.target.value.toString().trim().length==0) {
             // We can return string or jsx as the 'error' prop for the validated Component
               this.setState({isValid:false, errorMsg:event.target.placeholder+' Required! '});
               isInvalid=true;
         }
         break;
         case 'number':
         const re = /^[0-9\b]+$/;
         if (event.target.value != '' && !re.test(event.target.value)) {
            this.setState({isValid:false, errorMsg:'Numbers Only! '});
            isInvalid=true;
         }
         break;
         case 'email':
         const reg = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
           if (event.target.value != '' && !reg.test(event.target.value)){
             this.setState({isValid:false, errorMsg:'Email Id is invalid! '});
             isInvalid=true;
           }
         break;
       }

     }
     if(!isInvalid){
       this.setState({isValid:true, errorMsg:''});
     }
   }
   }

   render(){
     return (
       <div >
       <input {...this.props}  onBlur={(e)=>this.validate(e,this.props.validations)}/>
       <span ref='errorMsg' className='ErrorMsg'>{this.state.errorMsg}</span>
       <input type='hidden' className='error' value={!this.state.isValid}/>
       </div>);
   }
}

export default CustomInput;
