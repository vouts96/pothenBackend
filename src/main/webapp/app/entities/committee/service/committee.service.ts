import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommittee, NewCommittee } from '../committee.model';

export type PartialUpdateCommittee = Partial<ICommittee> & Pick<ICommittee, 'id'>;

export type EntityResponseType = HttpResponse<ICommittee>;
export type EntityArrayResponseType = HttpResponse<ICommittee[]>;

@Injectable({ providedIn: 'root' })
export class CommitteeService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/committees');

  create(committee: NewCommittee): Observable<EntityResponseType> {
    return this.http.post<ICommittee>(this.resourceUrl, committee, { observe: 'response' });
  }

  update(committee: ICommittee): Observable<EntityResponseType> {
    return this.http.put<ICommittee>(`${this.resourceUrl}/${this.getCommitteeIdentifier(committee)}`, committee, { observe: 'response' });
  }

  partialUpdate(committee: PartialUpdateCommittee): Observable<EntityResponseType> {
    return this.http.patch<ICommittee>(`${this.resourceUrl}/${this.getCommitteeIdentifier(committee)}`, committee, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICommittee>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommittee[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCommitteeIdentifier(committee: Pick<ICommittee, 'id'>): number {
    return committee.id;
  }

  compareCommittee(o1: Pick<ICommittee, 'id'> | null, o2: Pick<ICommittee, 'id'> | null): boolean {
    return o1 && o2 ? this.getCommitteeIdentifier(o1) === this.getCommitteeIdentifier(o2) : o1 === o2;
  }

  addCommitteeToCollectionIfMissing<Type extends Pick<ICommittee, 'id'>>(
    committeeCollection: Type[],
    ...committeesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const committees: Type[] = committeesToCheck.filter(isPresent);
    if (committees.length > 0) {
      const committeeCollectionIdentifiers = committeeCollection.map(committeeItem => this.getCommitteeIdentifier(committeeItem));
      const committeesToAdd = committees.filter(committeeItem => {
        const committeeIdentifier = this.getCommitteeIdentifier(committeeItem);
        if (committeeCollectionIdentifiers.includes(committeeIdentifier)) {
          return false;
        }
        committeeCollectionIdentifiers.push(committeeIdentifier);
        return true;
      });
      return [...committeesToAdd, ...committeeCollection];
    }
    return committeeCollection;
  }
}
