import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const authGuard = () => {
  const router = inject(Router);
  const token = localStorage.getItem('travelsphere_token');

  if (!token) {
    router.navigate(['/login']);
    return false;
  }
  return true;
};
