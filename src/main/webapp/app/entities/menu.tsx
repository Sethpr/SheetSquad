import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/character">
        Character
      </MenuItem>
      <MenuItem icon="asterisk" to="/archetype">
        Archetype
      </MenuItem>
      <MenuItem icon="asterisk" to="/stat">
        Stat
      </MenuItem>
      <MenuItem icon="asterisk" to="/skill">
        Skill
      </MenuItem>
      <MenuItem icon="asterisk" to="/pool">
        Pool
      </MenuItem>
      <MenuItem icon="asterisk" to="/power-category">
        Power Category
      </MenuItem>
      <MenuItem icon="asterisk" to="/power">
        Power
      </MenuItem>
      <MenuItem icon="asterisk" to="/quality">
        Quality
      </MenuItem>
      <MenuItem icon="asterisk" to="/extra">
        Extra
      </MenuItem>
      <MenuItem icon="asterisk" to="/base-extra">
        Base Extra
      </MenuItem>
      <MenuItem icon="asterisk" to="/refrence">
        Refrence
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
