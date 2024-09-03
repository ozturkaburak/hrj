import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './about-me.reducer';

export const AboutMeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const aboutMeEntity = useAppSelector(state => state.hr.aboutMe.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aboutMeDetailsHeading">
          <Translate contentKey="hrApp.aboutMe.detail.title">AboutMe</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{aboutMeEntity.id}</dd>
          <dt>
            <span id="socialMedia">
              <Translate contentKey="hrApp.aboutMe.socialMedia">Social Media</Translate>
            </span>
          </dt>
          <dd>{aboutMeEntity.socialMedia}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="hrApp.aboutMe.url">Url</Translate>
            </span>
          </dt>
          <dd>{aboutMeEntity.url}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hrApp.aboutMe.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{aboutMeEntity.createdAt ? <TextFormat value={aboutMeEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hrApp.aboutMe.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{aboutMeEntity.updatedAt ? <TextFormat value={aboutMeEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="hrApp.aboutMe.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>{aboutMeEntity.deletedAt ? <TextFormat value={aboutMeEntity.deletedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="hrApp.aboutMe.userProfile">User Profile</Translate>
          </dt>
          <dd>{aboutMeEntity.userProfile ? aboutMeEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/about-me" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/about-me/${aboutMeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AboutMeDetail;
