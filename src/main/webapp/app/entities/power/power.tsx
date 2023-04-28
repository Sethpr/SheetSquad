import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPower } from 'app/shared/model/power.model';
import { getEntities } from './power.reducer';

export const Power = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const powerList = useAppSelector(state => state.power.entities);
  const loading = useAppSelector(state => state.power.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="power-heading" data-cy="PowerHeading">
        Powers
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/power/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Power
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {powerList && powerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Cost</th>
                <th>Notes</th>
                <th>Owner</th>
                <th>Pool</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {powerList.map((power, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/power/${power.id}`} color="link" size="sm">
                      {power.id}
                    </Button>
                  </td>
                  <td>{power.name}</td>
                  <td>{power.cost}</td>
                  <td>{power.notes}</td>
                  <td>{power.owner ? <Link to={`/power-category/${power.owner.id}`}>{power.owner.id}</Link> : ''}</td>
                  <td>{power.pool ? <Link to={`/pool/${power.pool.id}`}>{power.pool.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/power/${power.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/power/${power.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/power/${power.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Powers found</div>
        )}
      </div>
    </div>
  );
};

export default Power;
