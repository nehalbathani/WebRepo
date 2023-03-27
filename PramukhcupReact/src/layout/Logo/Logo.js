import React from 'react';

import aplLogo from '../../assets/images/SiteLogo.JPG';
import classes from './Logo.css';

const logo = (props) => (
    <div className='Logo' style={{height: props.height}}>
        <img src={aplLogo} alt="MyLeague" />
    </div>
);

export default logo;
