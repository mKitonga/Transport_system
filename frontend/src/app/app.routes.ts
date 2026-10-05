import { Routes } from '@angular/router';

import { Home } from './features/home/home';
import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { Dashboard } from './features/dashboard/dashboard';

export const routes: Routes = [

    {
        path: '',
        component: Home
    },

    {
        path: 'login',
        component: Login
    },

    {
        path: 'register',
        component: Register
    },

    {
        path: 'dashboard',
        component: Dashboard
    }   

];