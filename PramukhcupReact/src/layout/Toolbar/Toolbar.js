import React, {Component} from 'react';
import './Toolbar.css';
import Logo from '../Logo/Logo';
import MenuIcon from '../MenuIcon/MenuIcon';

const Toolbar = (props) => {

    return (
      <div className="Toolbar">
        <MenuIcon changed={props.sidebarChange}/>

          <span> Avenel Cricket Club</span>
          <Logo/>
      </div>
    );

}

export default Toolbar;
