import {Injectable, OnDestroy} from '@angular/core';
import { MatPaginatorIntl } from '@angular/material/paginator';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PaginatorI18n implements OnDestroy{
  private langChangeSubscription: Subscription;
  private paginatorIntl: MatPaginatorIntl = new MatPaginatorIntl();
  lang= ""
  constructor(private readonly translate: TranslateService) {
    this.langChangeSubscription = this.translate.onLangChange.subscribe(() => {
      //console.log('Lang changed to', this.translate.currentLang);
      this.updatePaginatorLabels();
    });
  }

  ngOnDestroy() {
    if (this.langChangeSubscription) {
      this.langChangeSubscription.unsubscribe();
    }
  }

  getPaginatorIntl(): MatPaginatorIntl {
    this.updatePaginatorLabels();
    this.paginatorIntl.getRangeLabel = this.getRangeLabel.bind(this);
    return this.paginatorIntl;
  }

  private updatePaginatorLabels() {
    this.translate.stream(['ITEMS_PER_PAGE_LABEL', 'NEXT_PAGE_LABEL', 'PREVIOUS_PAGE_LABEL', 'FIRST_PAGE_LABEL', 'LAST_PAGE_LABEL']).subscribe(
      (translations) => {
        this.paginatorIntl.itemsPerPageLabel = translations['ITEMS_PER_PAGE_LABEL'];
        this.paginatorIntl.nextPageLabel = translations['NEXT_PAGE_LABEL'];
        this.paginatorIntl.previousPageLabel = translations['PREVIOUS_PAGE_LABEL'];
        this.paginatorIntl.firstPageLabel = translations['FIRST_PAGE_LABEL'];
        this.paginatorIntl.lastPageLabel = translations['LAST_PAGE_LABEL'];
      },
      (error) => {
        console.error('Translation error:', error);
      }
    );
  }

  private getRangeLabel(page: number, pageSize: number, length: number): string {
    if (length === 0 || pageSize === 0) {
      return this.translate.instant('RANGE_PAGE_LABEL_1', { length });
    }
    length = Math.max(length, 0);
    const startIndex = page * pageSize;
    const endIndex = startIndex < length ? Math.min(startIndex + pageSize, length) : startIndex + pageSize;
    return this.translate.instant('RANGE_PAGE_LABEL_2', { startIndex: startIndex + 1, endIndex, length });
  }
}
