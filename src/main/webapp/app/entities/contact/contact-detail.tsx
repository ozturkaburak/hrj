import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './contact.reducer';

export const ContactDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const contactEntity = useAppSelector(state => state.hr.contact.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contactDetailsHeading">
          <Translate contentKey="hrApp.contact.detail.title">Contact</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{contactEntity.id}</dd>
          <dt>
            <span id="secondaryEmail">
              <Translate contentKey="hrApp.contact.secondaryEmail">Secondary Email</Translate>
            </span>
          </dt>
          <dd>{contactEntity.secondaryEmail}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="hrApp.contact.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{contactEntity.phoneNumber}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hrApp.contact.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{contactEntity.createdAt ? <TextFormat value={contactEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hrApp.contact.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{contactEntity.updatedAt ? <TextFormat value={contactEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="hrApp.contact.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>{contactEntity.deletedAt ? <TextFormat value={contactEntity.deletedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="hrApp.contact.user">User</Translate>
          </dt>
          <dd>{contactEntity.user ? contactEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="hrApp.contact.city">City</Translate>
          </dt>
          <dd>{contactEntity.city ? contactEntity.city.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/contact" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/contact/${contactEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContactDetail;
