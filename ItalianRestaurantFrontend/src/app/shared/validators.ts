import {AbstractControl, ValidationErrors, ValidatorFn} from "@angular/forms";

export function priceValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const valid = /^[0-9]+(\.[0-9]{1,2})?$/.test(control.value);
    return valid ? null : { invalidPrice: true };
  };
}
