import React, {Component} from 'react';
import './Input.css';

const Input = (props) => {

  let outputCompJSX = '';
  switch(props.inputtype){
    case 'text':
      outputCompJSX= <input type='text' placeholder={"Enter "+props.lbltext} name={props.lbltext}  />;
    break;
    case 'radio':
      outputCompJSX = <input type='radio' name={props.groupname} value={props.val} />;
    break;
    case 'imgradio':
      outputCompJSX = <label>
                      <input type="radio" name={props.groupname} value={props.val} />
                      <img src={'../../assets/images/'+props.imgfilename}/>
                      </label>;
    break;
  }
    return (
      <div className='Input' >
      <label for={props.lbltext}>{props.lbltext}</label>
      {outputCompJSX}
      <label for={props.radiotxt}>{props.radiotxt}</label>

      </div>
    );

}

export default Input;
