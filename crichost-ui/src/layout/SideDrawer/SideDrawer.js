import React, {Component} from 'react';
import './SideDrawer.css';

const SideDrawer = () => {

    return (
      <div className="SideDrawer" >
        <a disabled> Home </a>
        <a disabled> League </a>
        <a disabled> Series </a>
        <a href="#"> Players </a>

      </div>
    );

}

export default SideDrawer;
