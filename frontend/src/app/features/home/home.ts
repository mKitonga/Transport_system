import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Navbar } from '../../shared/components/navbar/navbar';
import { Footer } from '../../shared/components/footer/footer';

@Component({
  selector: 'app-home',
  imports: [RouterModule, Navbar, Footer],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {

  constructor(private router: Router){}

  features = [

    {
      icon: ' 🚌',
      title: 'Sacco Management',
      description: 'Manage your Sacco operations efficiently with our comprehensive Sacco management features.'
    },

    {
      icon: '🏫',
      title: 'School Transport Management',
      description: 'Ensure the safety and efficiency of school transportation with our specialized school transport management tools.'
    },

    {
      icon: '👨‍👩‍👧‍👦',
      title: 'Parents Portal',
      description: 'Stay connected with your child\'s school transportation through our dedicated parents portal.'
    },

    {
      icon: '📍',
      title: 'Live Tracking',
      description: 'Track your vehicles in real-time with our advanced live tracking feature, ensuring timely arrivals and departures.'
    }
  ];

  steps = [

    {
      step: '1',
      title: 'Create an Account',
      description: 'Register as a Sacco Admin, Sacco Driver, School Admin, School Driver, or Parent to access the platform and its features.'
    },

    {
      step: '2',
      title: 'Manage Your Transport Operations',
      description: 'Add routes, vehicles, schools, students, and drivers from a single dashboard.'
    },

    {
      step: '3',
      title: 'Monitor Operations',
      description: 'Track Matatus and school buses, monitor activities, and receive real-time updates.'
    }
  ];

  goToLogin() {
    console.log('Login clicked');
    this.router.navigate(['/login']);
  }

  goToRegister() {
    console.log('Register clicked');
    this.router.navigate(['/register']);
  }


}
