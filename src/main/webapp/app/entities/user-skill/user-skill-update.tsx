import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISkill } from 'app/shared/model/skill.model';
import { getEntities as getSkills } from 'app/entities/skill/skill.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IUserSkill } from 'app/shared/model/user-skill.model';
import { SkillLevel } from 'app/shared/model/enumerations/skill-level.model';
import { getEntity, updateEntity, createEntity, reset } from './user-skill.reducer';

export const UserSkillUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const skills = useAppSelector(state => state.hr.skill.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const userSkillEntity = useAppSelector(state => state.hr.userSkill.entity);
  const loading = useAppSelector(state => state.hr.userSkill.loading);
  const updating = useAppSelector(state => state.hr.userSkill.updating);
  const updateSuccess = useAppSelector(state => state.hr.userSkill.updateSuccess);
  const skillLevelValues = Object.keys(SkillLevel);

  const handleClose = () => {
    navigate('/user-skill' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSkills({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.year !== undefined && typeof values.year !== 'number') {
      values.year = Number(values.year);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);
    values.deletedAt = convertDateTimeToServer(values.deletedAt);

    const entity = {
      ...userSkillEntity,
      ...values,
      skill: skills.find(it => it.id.toString() === values.skill?.toString()),
      user: users.find(it => it.id.toString() === values.user?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
          deletedAt: displayDefaultDateTime(),
        }
      : {
          level: 'EXPERT',
          ...userSkillEntity,
          createdAt: convertDateTimeFromServer(userSkillEntity.createdAt),
          updatedAt: convertDateTimeFromServer(userSkillEntity.updatedAt),
          deletedAt: convertDateTimeFromServer(userSkillEntity.deletedAt),
          skill: userSkillEntity?.skill?.id,
          user: userSkillEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hrApp.userSkill.home.createOrEditLabel" data-cy="UserSkillCreateUpdateHeading">
            <Translate contentKey="hrApp.userSkill.home.createOrEditLabel">Create or edit a UserSkill</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="user-skill-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('hrApp.userSkill.year')} id="user-skill-year" name="year" data-cy="year" type="text" />
              <ValidatedField label={translate('hrApp.userSkill.level')} id="user-skill-level" name="level" data-cy="level" type="select">
                {skillLevelValues.map(skillLevel => (
                  <option value={skillLevel} key={skillLevel}>
                    {translate('hrApp.SkillLevel.' + skillLevel)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('hrApp.userSkill.createdAt')}
                id="user-skill-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.userSkill.updatedAt')}
                id="user-skill-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hrApp.userSkill.deletedAt')}
                id="user-skill-deletedAt"
                name="deletedAt"
                data-cy="deletedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="user-skill-skill" name="skill" data-cy="skill" label={translate('hrApp.userSkill.skill')} type="select">
                <option value="" key="0" />
                {skills
                  ? skills.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="user-skill-user" name="user" data-cy="user" label={translate('hrApp.userSkill.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-skill" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default UserSkillUpdate;
