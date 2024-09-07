import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/job-posting">
        <Translate contentKey="global.menu.entities.jobPosting" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/resume">
        <Translate contentKey="global.menu.entities.resume" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-profile">
        <Translate contentKey="global.menu.entities.userProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/upload">
        <Translate contentKey="global.menu.entities.upload" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/experience">
        <Translate contentKey="global.menu.entities.experience" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/education">
        <Translate contentKey="global.menu.entities.education" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/certificate">
        <Translate contentKey="global.menu.entities.certificate" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/about-me">
        <Translate contentKey="global.menu.entities.aboutMe" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-skill">
        <Translate contentKey="global.menu.entities.userSkill" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/skill">
        <Translate contentKey="global.menu.entities.skill" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/contact">
        <Translate contentKey="global.menu.entities.contact" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/city">
        <Translate contentKey="global.menu.entities.city" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/country">
        <Translate contentKey="global.menu.entities.country" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-language">
        <Translate contentKey="global.menu.entities.userLanguage" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/language">
        <Translate contentKey="global.menu.entities.language" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-assignment">
        <Translate contentKey="global.menu.entities.userAssignment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/assignment">
        <Translate contentKey="global.menu.entities.assignment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/question">
        <Translate contentKey="global.menu.entities.question" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/answer">
        <Translate contentKey="global.menu.entities.answer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/company">
        <Translate contentKey="global.menu.entities.company" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
