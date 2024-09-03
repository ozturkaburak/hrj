import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ILanguage } from 'app/shared/model/language.model';
import { getEntities as getLanguages } from 'app/entities/language/language.reducer';
import { IUserLanguage } from 'app/shared/model/user-language.model';
import { LanguageLevel } from 'app/shared/model/enumerations/language-level.model';
import { getEntity, updateEntity, createEntity, reset } from './user-language.reducer';

export const UserLanguageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const languages = useAppSelector(state => state.hr.language.entities);
  const userLanguageEntity = useAppSelector(state => state.hr.userLanguage.entity);
  const loading = useAppSelector(state => state.hr.userLanguage.loading);
  const updating = useAppSelector(state => state.hr.userLanguage.updating);
  const updateSuccess = useAppSelector(state => state.hr.userLanguage.updateSuccess);
  const languageLevelValues = Object.keys(LanguageLevel);

  const handleClose = () => {
    navigate('/user-language' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getLanguages({}));
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
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);
    values.deletedAt = convertDateTimeToServer(values.deletedAt);

    const entity = {
      ...userLanguageEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      language: languages.find(it => it.id.toString() === values.language?.toString()),
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
          level: 'BEGINNER',
          ...userLanguageEntity,
          createdAt: convertDateTimeFromServer(userLanguageEntity.createdAt),
          updatedAt: convertDateTimeFromServer(userLanguageEntity.updatedAt),
          deletedAt: convertDateTimeFromServer(userLanguageEntity.deletedAt),
          user: userLanguageEntity?.user?.id,
          language: userLanguageEntity?.language?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hrApp.userLanguage.home.createOrEditLabel" data-cy="UserLanguageCreateUpdateHeading">
            <Translate contentKey="hrApp.userLanguage.home.createOrEditLabel">Create or edit a UserLanguage</Translate>
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
                  id="user-language-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hrApp.userLanguage.level')}
                id="user-language-level"
                name="level"
                data-cy="level"
                type="select"
              >
                {languageLevelValues.map(languageLevel => (
                  <option value={languageLevel} key={languageLevel}>
                    {translate('hrApp.LanguageLevel.' + languageLevel)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('hrApp.userLanguage.nativeLanguage')}
                id="user-language-nativeLanguage"
                name="nativeLanguage"
                data-cy="nativeLanguage"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('hrApp.userLanguage.createdAt')}
                id="user-language-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.userLanguage.updatedAt')}
                id="user-language-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hrApp.userLanguage.deletedAt')}
                id="user-language-deletedAt"
                name="deletedAt"
                data-cy="deletedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="user-language-user" name="user" data-cy="user" label={translate('hrApp.userLanguage.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-language-language"
                name="language"
                data-cy="language"
                label={translate('hrApp.userLanguage.language')}
                type="select"
              >
                <option value="" key="0" />
                {languages
                  ? languages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-language" replace color="info">
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

export default UserLanguageUpdate;
