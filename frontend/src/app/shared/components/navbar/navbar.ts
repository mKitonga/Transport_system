import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar implements OnInit {

  isDarkMode = false;

  constructor(private router: Router) {}

  ngOnInit(): void {

    const savedTheme = localStorage.getItem('theme');

    if (savedTheme === 'dark') {

      this.isDarkMode = true;

      document.body.classList.add('dark-mode');

    } else {

      this.isDarkMode = false;

      document.body.classList.remove('dark-mode');
    }
  }


  goHome(): void {

    this.router.navigate(['/']);

  }


  login(): void {

    this.router.navigate(['/login']);

  }


  register(): void {

    this.router.navigate(['/register']);

  }


  toggleTheme(): void {

    this.isDarkMode = !this.isDarkMode;

    if (this.isDarkMode) {

        document.body.classList.add('dark-mode');

        localStorage.setItem('theme', 'dark');

    } else {

        document.body.classList.remove('dark-mode');

        localStorage.setItem('theme', 'light');

    }

    console.log('Dark mode:', this.isDarkMode);
    console.log('Body classes:', document.body.className);
}

}