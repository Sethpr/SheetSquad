import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICharacter } from 'app/shared/model/character.model';
import { getEntities } from './character.reducer';

export const Character = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const characterList = useAppSelector(state => state.character.entities);
  const loading = useAppSelector(state => state.character.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="character-heading" data-cy="CharacterHeading">
        Characters
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/character/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Character
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {characterList && characterList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Talent Name</th>
                <th>Loyalty</th>
                <th>Passion</th>
                <th>Inventory</th>
                <th>Point Total</th>
                <th>Spent Points</th>
                <th>Archetype</th>
                <th>Owner</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {characterList.map((character, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/character/${character.id}`} color="link" size="sm">
                      {character.id}
                    </Button>
                  </td>
                  <td>{character.name}</td>
                  <td>{character.talentName}</td>
                  <td>{character.loyalty}</td>
                  <td>{character.passion}</td>
                  <td>{character.inventory}</td>
                  <td>{character.pointTotal}</td>
                  <td>{character.spentPoints}</td>
                  <td>{character.archetype ? <Link to={`/archetype/${character.archetype.id}`}>{character.archetype.id}</Link> : ''}</td>
                  <td>{character.owner ? character.owner.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/character/${character.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/character/${character.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/character/${character.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Characters found</div>
        )}
      </div>
    </div>
  );
};

export default Character;
