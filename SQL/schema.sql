--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5
-- Dumped by pg_dump version 17.5

-- Started on 2025-07-01 19:07:28

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 17977)
-- Name: commento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.commento (
    id integer NOT NULL,
    autore_ssn character varying(16),
    documento_id integer,
    contenuto text
);


ALTER TABLE public.commento OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 17982)
-- Name: commento_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.commento_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.commento_id_seq OWNER TO postgres;

--
-- TOC entry 4972 (class 0 OID 0)
-- Dependencies: 218
-- Name: commento_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.commento_id_seq OWNED BY public.commento.id;


--
-- TOC entry 219 (class 1259 OID 17983)
-- Name: documento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.documento (
    id integer NOT NULL,
    titolo character varying(100),
    contenuto text,
    team_nome character varying(100)
);


ALTER TABLE public.documento OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 17988)
-- Name: documento_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.documento_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.documento_id_seq OWNER TO postgres;

--
-- TOC entry 4973 (class 0 OID 0)
-- Dependencies: 220
-- Name: documento_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.documento_id_seq OWNED BY public.documento.id;


--
-- TOC entry 221 (class 1259 OID 17989)
-- Name: giudice_hackathon; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.giudice_hackathon (
    giudice_ssn character varying(16) NOT NULL,
    hackathon_titolo character varying(100) NOT NULL,
    stato character varying(20) DEFAULT 'INVITATO'::character varying
);


ALTER TABLE public.giudice_hackathon OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 17993)
-- Name: hackathon; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hackathon (
    titolo character varying(100) NOT NULL,
    sede character varying(100),
    data_inizio date,
    durata integer,
    descrizione_problema text,
    organizzatore_ssn character varying(16),
    num_max_iscritti integer,
    num_max_per_team integer
);


ALTER TABLE public.hackathon OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 17998)
-- Name: partecipante_hackathon; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.partecipante_hackathon (
    partecipante_ssn character varying(16) NOT NULL,
    hackathon_titolo character varying(100) NOT NULL
);


ALTER TABLE public.partecipante_hackathon OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 18001)
-- Name: partecipante_team; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.partecipante_team (
    partecipante_ssn character varying(16) NOT NULL,
    team_nome character varying(100) NOT NULL
);


ALTER TABLE public.partecipante_team OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 18004)
-- Name: team; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.team (
    nome character varying(100) NOT NULL,
    evento_titolo character varying(100)
);


ALTER TABLE public.team OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 18007)
-- Name: utente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utente (
    ssn character varying(16) NOT NULL,
    nome character varying(50) NOT NULL,
    cognome character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    password character varying(100) NOT NULL,
    ruolo character varying(20) NOT NULL
);


ALTER TABLE public.utente OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 18010)
-- Name: voto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.voto (
    giudice_ssn character varying(16) NOT NULL,
    team_nome character varying(100) NOT NULL,
    punteggio integer
);


ALTER TABLE public.voto OWNER TO postgres;

--
-- TOC entry 4775 (class 2604 OID 18013)
-- Name: commento id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commento ALTER COLUMN id SET DEFAULT nextval('public.commento_id_seq'::regclass);


--
-- TOC entry 4776 (class 2604 OID 18014)
-- Name: documento id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documento ALTER COLUMN id SET DEFAULT nextval('public.documento_id_seq'::regclass);


--
-- TOC entry 4956 (class 0 OID 17977)
-- Dependencies: 217
-- Data for Name: commento; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.commento (id, autore_ssn, documento_id, contenuto) FROM stdin;
\.


--
-- TOC entry 4958 (class 0 OID 17983)
-- Dependencies: 219
-- Data for Name: documento; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.documento (id, titolo, contenuto, team_nome) FROM stdin;
\.


--
-- TOC entry 4960 (class 0 OID 17989)
-- Dependencies: 221
-- Data for Name: giudice_hackathon; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.giudice_hackathon (giudice_ssn, hackathon_titolo, stato) FROM stdin;
\.


--
-- TOC entry 4961 (class 0 OID 17993)
-- Dependencies: 222
-- Data for Name: hackathon; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hackathon (titolo, sede, data_inizio, durata, descrizione_problema, organizzatore_ssn, num_max_iscritti, num_max_per_team) FROM stdin;
\.


--
-- TOC entry 4962 (class 0 OID 17998)
-- Dependencies: 223
-- Data for Name: partecipante_hackathon; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.partecipante_hackathon (partecipante_ssn, hackathon_titolo) FROM stdin;
\.


--
-- TOC entry 4963 (class 0 OID 18001)
-- Dependencies: 224
-- Data for Name: partecipante_team; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.partecipante_team (partecipante_ssn, team_nome) FROM stdin;
\.


--
-- TOC entry 4964 (class 0 OID 18004)
-- Dependencies: 225
-- Data for Name: team; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.team (nome, evento_titolo) FROM stdin;
\.


--
-- TOC entry 4965 (class 0 OID 18007)
-- Dependencies: 226
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.utente (ssn, nome, cognome, email, password, ruolo) FROM stdin;
1			p	a	Partecipante
\.


--
-- TOC entry 4966 (class 0 OID 18010)
-- Dependencies: 227
-- Data for Name: voto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.voto (giudice_ssn, team_nome, punteggio) FROM stdin;
\.


--
-- TOC entry 4974 (class 0 OID 0)
-- Dependencies: 218
-- Name: commento_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.commento_id_seq', 1, false);


--
-- TOC entry 4975 (class 0 OID 0)
-- Dependencies: 220
-- Name: documento_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.documento_id_seq', 1, true);


--
-- TOC entry 4779 (class 2606 OID 18016)
-- Name: commento commento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commento
    ADD CONSTRAINT commento_pkey PRIMARY KEY (id);


--
-- TOC entry 4781 (class 2606 OID 18018)
-- Name: documento documento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documento
    ADD CONSTRAINT documento_pkey PRIMARY KEY (id);


--
-- TOC entry 4783 (class 2606 OID 18020)
-- Name: giudice_hackathon giudice_hackathon_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.giudice_hackathon
    ADD CONSTRAINT giudice_hackathon_pkey PRIMARY KEY (giudice_ssn, hackathon_titolo);


--
-- TOC entry 4785 (class 2606 OID 18022)
-- Name: hackathon hackathon_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hackathon
    ADD CONSTRAINT hackathon_pkey PRIMARY KEY (titolo);


--
-- TOC entry 4787 (class 2606 OID 18024)
-- Name: partecipante_hackathon partecipante_hackathon_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.partecipante_hackathon
    ADD CONSTRAINT partecipante_hackathon_pkey PRIMARY KEY (partecipante_ssn, hackathon_titolo);


--
-- TOC entry 4789 (class 2606 OID 18026)
-- Name: partecipante_team partecipante_team_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.partecipante_team
    ADD CONSTRAINT partecipante_team_pkey PRIMARY KEY (partecipante_ssn, team_nome);


--
-- TOC entry 4791 (class 2606 OID 18028)
-- Name: team team_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.team
    ADD CONSTRAINT team_pkey PRIMARY KEY (nome);


--
-- TOC entry 4793 (class 2606 OID 18030)
-- Name: utente utente_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_email_key UNIQUE (email);


--
-- TOC entry 4795 (class 2606 OID 18032)
-- Name: utente utente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (ssn);


--
-- TOC entry 4797 (class 2606 OID 18034)
-- Name: voto voto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voto
    ADD CONSTRAINT voto_pkey PRIMARY KEY (giudice_ssn, team_nome);


--
-- TOC entry 4798 (class 2606 OID 18035)
-- Name: commento commento_autore_ssn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commento
    ADD CONSTRAINT commento_autore_ssn_fkey FOREIGN KEY (autore_ssn) REFERENCES public.utente(ssn);


--
-- TOC entry 4799 (class 2606 OID 18040)
-- Name: commento commento_documento_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commento
    ADD CONSTRAINT commento_documento_id_fkey FOREIGN KEY (documento_id) REFERENCES public.documento(id);


--
-- TOC entry 4800 (class 2606 OID 18045)
-- Name: documento documento_team_nome_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documento
    ADD CONSTRAINT documento_team_nome_fkey FOREIGN KEY (team_nome) REFERENCES public.team(nome);


--
-- TOC entry 4801 (class 2606 OID 18050)
-- Name: giudice_hackathon giudice_hackathon_giudice_ssn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.giudice_hackathon
    ADD CONSTRAINT giudice_hackathon_giudice_ssn_fkey FOREIGN KEY (giudice_ssn) REFERENCES public.utente(ssn);


--
-- TOC entry 4802 (class 2606 OID 18055)
-- Name: giudice_hackathon giudice_hackathon_hackathon_titolo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.giudice_hackathon
    ADD CONSTRAINT giudice_hackathon_hackathon_titolo_fkey FOREIGN KEY (hackathon_titolo) REFERENCES public.hackathon(titolo);


--
-- TOC entry 4803 (class 2606 OID 18060)
-- Name: hackathon hackathon_organizzatore_ssn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hackathon
    ADD CONSTRAINT hackathon_organizzatore_ssn_fkey FOREIGN KEY (organizzatore_ssn) REFERENCES public.utente(ssn);


--
-- TOC entry 4804 (class 2606 OID 18065)
-- Name: partecipante_hackathon partecipante_hackathon_hackathon_titolo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.partecipante_hackathon
    ADD CONSTRAINT partecipante_hackathon_hackathon_titolo_fkey FOREIGN KEY (hackathon_titolo) REFERENCES public.hackathon(titolo);


--
-- TOC entry 4805 (class 2606 OID 18070)
-- Name: partecipante_hackathon partecipante_hackathon_partecipante_ssn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.partecipante_hackathon
    ADD CONSTRAINT partecipante_hackathon_partecipante_ssn_fkey FOREIGN KEY (partecipante_ssn) REFERENCES public.utente(ssn);


--
-- TOC entry 4806 (class 2606 OID 18075)
-- Name: partecipante_team partecipante_team_partecipante_ssn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.partecipante_team
    ADD CONSTRAINT partecipante_team_partecipante_ssn_fkey FOREIGN KEY (partecipante_ssn) REFERENCES public.utente(ssn);


--
-- TOC entry 4807 (class 2606 OID 18080)
-- Name: partecipante_team partecipante_team_team_nome_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.partecipante_team
    ADD CONSTRAINT partecipante_team_team_nome_fkey FOREIGN KEY (team_nome) REFERENCES public.team(nome);


--
-- TOC entry 4808 (class 2606 OID 18085)
-- Name: team team_evento_titolo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.team
    ADD CONSTRAINT team_evento_titolo_fkey FOREIGN KEY (evento_titolo) REFERENCES public.hackathon(titolo);


--
-- TOC entry 4809 (class 2606 OID 18090)
-- Name: voto voto_giudice_ssn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voto
    ADD CONSTRAINT voto_giudice_ssn_fkey FOREIGN KEY (giudice_ssn) REFERENCES public.utente(ssn);


--
-- TOC entry 4810 (class 2606 OID 18095)
-- Name: voto voto_team_nome_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voto
    ADD CONSTRAINT voto_team_nome_fkey FOREIGN KEY (team_nome) REFERENCES public.team(nome);


-- Completed on 2025-07-01 19:07:28

--
-- PostgreSQL database dump complete
--

