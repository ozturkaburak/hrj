import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './experience.reducer';

export const ExperienceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const experienceEntity = useAppSelector(state => state.hr.experience.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="experienceDetailsHeading">
          <Translate contentKey="hrApp.experience.detail.title">Experience</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{experienceEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="hrApp.experience.title">Title</Translate>
            </span>
          </dt>
          <dd>{experienceEntity.title}</dd>
          <dt>
            <span id="companyName">
              <Translate contentKey="hrApp.experience.companyName">Company Name</Translate>
            </span>
          </dt>
          <dd>{experienceEntity.companyName}</dd>
          <dt>
            <span id="workType">
              <Translate contentKey="hrApp.experience.workType">Work Type</Translate>
            </span>
          </dt>
          <dd>{experienceEntity.workType}</dd>
          <dt>
            <span id="contractType">
              <Translate contentKey="hrApp.experience.contractType">Contract Type</Translate>
            </span>
          </dt>
          <dd>{experienceEntity.contractType}</dd>
          <dt>
            <span id="officeLocation">
              <Translate contentKey="hrApp.experience.officeLocation">Office Location</Translate>
            </span>
          </dt>
          <dd>{experienceEntity.officeLocation}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="hrApp.experience.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {experienceEntity.startDate ? <TextFormat value={experienceEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="hrApp.experience.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>{experienceEntity.endDate ? <TextFormat value={experienceEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="hrApp.experience.description">Description</Translate>
            </span>
          </dt>
          <dd>{experienceEntity.description}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hrApp.experience.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {experienceEntity.createdAt ? <TextFormat value={experienceEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hrApp.experience.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {experienceEntity.updatedAt ? <TextFormat value={experienceEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="hrApp.experience.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>
            {experienceEntity.deletedAt ? <TextFormat value={experienceEntity.deletedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="hrApp.experience.userProfile">User Profile</Translate>
          </dt>
          <dd>{experienceEntity.userProfile ? experienceEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/experience" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/experience/${experienceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExperienceDetail;
