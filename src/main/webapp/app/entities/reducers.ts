import jobPosting from 'app/entities/job-posting/job-posting.reducer';
import resume from 'app/entities/resume/resume.reducer';
import userProfile from 'app/entities/user-profile/user-profile.reducer';
import upload from 'app/entities/upload/upload.reducer';
import experience from 'app/entities/experience/experience.reducer';
import education from 'app/entities/education/education.reducer';
import certificate from 'app/entities/certificate/certificate.reducer';
import aboutMe from 'app/entities/about-me/about-me.reducer';
import userSkill from 'app/entities/user-skill/user-skill.reducer';
import skill from 'app/entities/skill/skill.reducer';
import contact from 'app/entities/contact/contact.reducer';
import city from 'app/entities/city/city.reducer';
import country from 'app/entities/country/country.reducer';
import userLanguage from 'app/entities/user-language/user-language.reducer';
import language from 'app/entities/language/language.reducer';
import userAssignment from 'app/entities/user-assignment/user-assignment.reducer';
import assignment from 'app/entities/assignment/assignment.reducer';
import question from 'app/entities/question/question.reducer';
import answer from 'app/entities/answer/answer.reducer';
import company from 'app/entities/company/company.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  jobPosting,
  resume,
  userProfile,
  upload,
  experience,
  education,
  certificate,
  aboutMe,
  userSkill,
  skill,
  contact,
  city,
  country,
  userLanguage,
  language,
  userAssignment,
  assignment,
  question,
  answer,
  company,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
