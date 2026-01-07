import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../../services/chat.service';
import { AuthService } from '../../services/auth.service';

interface Message {
  text: string;
  sender: 'user' | 'agent';
}

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css'
})
export class ChatComponent implements OnInit {
  private chatService = inject(ChatService);
  private authService = inject(AuthService);
  private router = inject(Router);

  messages: Message[] = [];
  newMessage = '';
  isTyping = false;
  isAdmin = false;

  ngOnInit(): void {
    this.checkAdminRole();
    this.messages.push({
      text: 'Bonjour ! Je suis votre assistant IA. Comment puis-je vous aider aujourd\'hui ?',
      sender: 'agent'
    });
  }

  checkAdminRole(): void {
    const token = this.authService.getToken();
    if (token) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        if (payload.roles && payload.roles.includes('ADMIN')) {
          this.isAdmin = true;
        }
      } catch (e) {
        console.error('Error parsing token', e);
      }
    }
  }

  // ... (rest of sendMessage logic) ...
  sendMessage(): void {
    if (!this.newMessage.trim()) return;

    const userMsg = this.newMessage;
    this.messages.push({ text: userMsg, sender: 'user' });
    this.newMessage = '';
    this.isTyping = true;

    // Create a placeholder for the agent's streaming response
    const agentMsg: Message = { text: '', sender: 'agent' };
    this.messages.push(agentMsg);

    this.chatService.streamChat(userMsg).subscribe({
      next: (chunk) => {
        agentMsg.text += chunk;
      },
      error: (err) => {
        console.error(err);
        agentMsg.text = `⚠️ ${err}`;
        this.isTyping = false;
      },
      complete: () => {
        this.isTyping = false;
      }
    });
  }

  goToAdmin(): void {
    this.router.navigate(['/admin']);
  }

  logout(): void {
    this.authService.logout();
  }
}
