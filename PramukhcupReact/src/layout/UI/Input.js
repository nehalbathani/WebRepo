import React, {Component} from 'react';
import Dropdown from './Dropdown/Dropdown';
import CustomInput from './CustomInput/CustomInput';
import './Input.css';
import BOALogo from '../../assets/images/BOALogo.png';
import ChaseLogo from '../../assets/images/ChaseLogo.png';
import PaypalLogo from '../../assets/images/PaypalLogo.png';


const Input = (props) => {

  let outputCompJSX = '';
  switch(props.inputtype){
    case 'text':
      outputCompJSX= <CustomInput type='text' id={props.id} onChange={props.changed} onFocus={props.onFocus} onBlur={props.onBlur} placeholder={props.placeHolderText} name={props.ctrlname} validations={props.validations} />;
    break;
    case 'number':
      outputCompJSX= <CustomInput type='text' id={props.id} onChange={props.changed}  placeholder={props.placeHolderText} name={props.ctrlname} validations={props.validations}/>;
    break;
    case 'radio':
      outputCompJSX = <input type='radio' name={props.groupname} value={props.val} onChange={props.changed}/>;
    break;
    case 'dropdown':
      outputCompJSX = <Dropdown id={props.id} selname={props.ctrlname} data={props.data} onChange={props.changed}/>;
    break;
    case 'label':
      outputCompJSX = <label> {props.lbltext} </label>;
    break;
    case 'imgradio':
      let imgobj=null;
      if(props.img=='BOA') {
        imgobj=BOALogo;
      } else if (props.img=='Chase') {
        imgobj=ChaseLogo;
      }else if (props.img=='PayPal') {
        imgobj=PaypalLogo;
      }
      outputCompJSX = <label>
                      <input type="radio" name={props.groupname} value={props.val} onChange={props.changed} />
                      <img src={imgobj}/>
                      </label>;
    break;
  }
  let radiolbltxt='';
  if(props.radiotxt){
    radiolbltxt=<label htmlFor={props.radiotxt}>{props.radiotxt}</label>;
  }
    return (
      <div className='Input' >
      {outputCompJSX}
      {radiolbltxt}
      </div>
    );

}


export default Input;
