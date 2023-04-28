import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Character from './character';
import Archetype from './archetype';
import Stat from './stat';
import Skill from './skill';
import Pool from './pool';
import PowerCategory from './power-category';
import Power from './power';
import Quality from './quality';
import Extra from './extra';
import BaseExtra from './base-extra';
import Refrence from './refrence';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="character/*" element={<Character />} />
        <Route path="archetype/*" element={<Archetype />} />
        <Route path="stat/*" element={<Stat />} />
        <Route path="skill/*" element={<Skill />} />
        <Route path="pool/*" element={<Pool />} />
        <Route path="power-category/*" element={<PowerCategory />} />
        <Route path="power/*" element={<Power />} />
        <Route path="quality/*" element={<Quality />} />
        <Route path="extra/*" element={<Extra />} />
        <Route path="base-extra/*" element={<BaseExtra />} />
        <Route path="refrence/*" element={<Refrence />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
