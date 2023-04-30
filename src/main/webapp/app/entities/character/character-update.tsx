import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IArchetype } from 'app/shared/model/archetype.model';
import { getEntities as getArchetypes } from 'app/entities/archetype/archetype.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IStat } from 'app/shared/model/stat.model';
import { getEntities as getStats } from 'app/entities/stat/stat.reducer';
import { ISkill } from 'app/shared/model/skill.model';
import { getEntities as getSkills } from 'app/entities/skill/skill.reducer';
import { ICharacter } from 'app/shared/model/character.model';
import { getEntity, updateEntity, createEntity, reset } from './character.reducer';

export const CharacterUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const archetypes = useAppSelector(state => state.archetype.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const stats = useAppSelector(state => state.stat.entities);
  const skills = useAppSelector(state => state.skill.entities);
  const characterEntity = useAppSelector(state => state.character.entity);
  const loading = useAppSelector(state => state.character.loading);
  const updating = useAppSelector(state => state.character.updating);
  const updateSuccess = useAppSelector(state => state.character.updateSuccess);

  const handleClose = () => {
    navigate('/character');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getArchetypes({}));
    dispatch(getUsers({}));
    dispatch(getStats({}));
    dispatch(getSkills({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...characterEntity,
      ...values,
      archetype: archetypes.find(it => it.id.toString() === values.archetype.toString()),
      owner: users.find(it => it.id.toString() === values.owner.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...characterEntity,
          archetype: characterEntity?.archetype?.id,
          owner: characterEntity?.owner?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheetSquadApp.character.home.createOrEditLabel" data-cy="CharacterCreateUpdateHeading">
            Create or edit a Character
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="character-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="character-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Talent Name" id="character-talentName" name="talentName" data-cy="talentName" type="text" />
              <ValidatedField label="Loyalty" id="character-loyalty" name="loyalty" data-cy="loyalty" type="text" />
              <ValidatedField label="Passion" id="character-passion" name="passion" data-cy="passion" type="text" />
              <ValidatedField label="Inventory" id="character-inventory" name="inventory" data-cy="inventory" type="text" />
              <ValidatedField
                label="Point Total"
                id="character-pointTotal"
                name="pointTotal"
                data-cy="pointTotal"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Spent Points"
                id="character-spentPoints"
                name="spentPoints"
                data-cy="spentPoints"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField id="character-archetype" name="archetype" data-cy="archetype" label="Archetype" type="select">
                <option value="" key="0" />
                {archetypes
                  ? archetypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="character-owner" name="owner" data-cy="owner" label="Owner" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/character" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CharacterUpdate;
