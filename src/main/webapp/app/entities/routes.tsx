import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import entitiesReducers from './reducers';

import JobPosting from './job-posting';
import Resume from './resume';
import UserProfile from './user-profile';
import Upload from './upload';
import Experience from './experience';
import Education from './education';
import Certificate from './certificate';
import AboutMe from './about-me';
import UserSkill from './user-skill';
import Skill from './skill';
import Contact from './contact';
import City from './city';
import Country from './country';
import UserLanguage from './user-language';
import Language from './language';
import UserAssignment from './user-assignment';
import Assignment from './assignment';
import Question from './question';
import Answer from './answer';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('hr', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="job-posting/*" element={<JobPosting />} />
        <Route path="resume/*" element={<Resume />} />
        <Route path="user-profile/*" element={<UserProfile />} />
        <Route path="upload/*" element={<Upload />} />
        <Route path="experience/*" element={<Experience />} />
        <Route path="education/*" element={<Education />} />
        <Route path="certificate/*" element={<Certificate />} />
        <Route path="about-me/*" element={<AboutMe />} />
        <Route path="user-skill/*" element={<UserSkill />} />
        <Route path="skill/*" element={<Skill />} />
        <Route path="contact/*" element={<Contact />} />
        <Route path="city/*" element={<City />} />
        <Route path="country/*" element={<Country />} />
        <Route path="user-language/*" element={<UserLanguage />} />
        <Route path="language/*" element={<Language />} />
        <Route path="user-assignment/*" element={<UserAssignment />} />
        <Route path="assignment/*" element={<Assignment />} />
        <Route path="question/*" element={<Question />} />
        <Route path="answer/*" element={<Answer />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
