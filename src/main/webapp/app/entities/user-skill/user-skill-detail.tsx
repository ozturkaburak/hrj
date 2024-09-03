import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-skill.reducer';

export const UserSkillDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userSkillEntity = useAppSelector(state => state.hr.userSkill.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userSkillDetailsHeading">
          <Translate contentKey="hrApp.userSkill.detail.title">UserSkill</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userSkillEntity.id}</dd>
          <dt>
            <span id="year">
              <Translate contentKey="hrApp.userSkill.year">Year</Translate>
            </span>
          </dt>
          <dd>{userSkillEntity.year}</dd>
          <dt>
            <span id="level">
              <Translate contentKey="hrApp.userSkill.level">Level</Translate>
            </span>
          </dt>
          <dd>{userSkillEntity.level}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hrApp.userSkill.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {userSkillEntity.createdAt ? <TextFormat value={userSkillEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hrApp.userSkill.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {userSkillEntity.updatedAt ? <TextFormat value={userSkillEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="hrApp.userSkill.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>
            {userSkillEntity.deletedAt ? <TextFormat value={userSkillEntity.deletedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="hrApp.userSkill.skill">Skill</Translate>
          </dt>
          <dd>{userSkillEntity.skill ? userSkillEntity.skill.id : ''}</dd>
          <dt>
            <Translate contentKey="hrApp.userSkill.user">User</Translate>
          </dt>
          <dd>{userSkillEntity.user ? userSkillEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-skill" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-skill/${userSkillEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserSkillDetail;
