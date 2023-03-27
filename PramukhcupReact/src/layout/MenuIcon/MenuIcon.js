import React, {Component} from 'react';
import './MenuIcon.css';


const MenuIcon = (props) => {

    return (
      <div className="menuicon" onClick={props.changed}>
        <div className="bar1"></div>
        <div className="bar2"></div>
        <div className="bar3"></div>
      </div>
    );

}

export default MenuIcon;
