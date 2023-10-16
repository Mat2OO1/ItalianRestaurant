import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminTableQrComponent } from './admin-table-qr.component';

describe('AdminTableQrComponent', () => {
  let component: AdminTableQrComponent;
  let fixture: ComponentFixture<AdminTableQrComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminTableQrComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminTableQrComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
