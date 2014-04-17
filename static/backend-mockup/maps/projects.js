var _projects = {
    list: [
        {
            project: {
                id: 101,
                title: 'PIRATE Telescope',
                author: {
                    id: 1,
                    username: 'The Open University'
                },
                type: 'challenge',
                activity: {
                    responses: 23,
                    ending: 0
                },
                description: {
                    teaser: 'har har har',
                    image: 'http://pirate.open.ac.uk/PIRATE_files/IMG_2861_CDK17_web.JPG'
                },
                open: true
            },
            access: {
                member: true,
                admin: false,
                author: false
            },
            data: {
                members: 30,
                responses: 23
            }
        },
        {
            project: {
                id: 102,
                title: 'Fastest lift',
                type: 'senseit',
                activity: {
                },
                author: {
                    id: 2,
                    username: 'nQuire Young Citizen project'
                },
                description: {
                    teaser: 'Find the fastest lift in the world or your country. Share data with other users.',
                    image: 'http://upload.wikimedia.org/wikipedia/commons/thumb/9/9b/Licenciado_Gustavo_D%C3%ADaz_Ordaz_International_Airport_%282014%29_-_03.JPG/450px-Licenciado_Gustavo_D%C3%ADaz_Ordaz_International_Airport_%282014%29_-_03.JPG'
                },
                open: true
            },
            access: {
                member: true,
                admin: false,
                author: false
            },
            data: {
                members: 40,
                series: 230
            }
        },
        {
            project: {
                id: 103,
                title: 'Bumble bees',
                type: 'spotit',
                author: {
                    id: 3,
                    username: 'BSA'
                },
                activity: {
                },
                description: {
                    teaser: "Help us find out what's happening to bumble bees!",
                    image: 'http://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/Bumblebee_October_2007-3a.jpg/800px-Bumblebee_October_2007-3a.jpg'
                },
                open: true
            },
            access: {
                member: true,
                admin: false,
                author: false
            },
            data: {
                members: 40,
                spotted: 45
            }
        },
        {
            project: {
                id: 104,
                title: 'My PIRATE Telescope',
                author: {
                    id: 1,
                    username: 'The Open University'
                },
                type: 'challenge',
                activity: {
                    responses: 23,
                    ending: 0
                },
                description: {
                    teaser: 'har har har',
                    image: 'http://pirate.open.ac.uk/PIRATE_files/IMG_2861_CDK17_web.JPG',
                    blocks: [
                        {type: 'text', title: 'Why', content: 'Because we love telescopes.'},
                        {type: 'www',where: 'Majorca, Spain / Outer space',when: 'May 2014',who: 'The Open University'},
                        {type: 'text', title: 'How', content: 'Just propose a good idea on how to use the PIRATE Telescope. If you are selected, you will be able to use the telescope for one night.'}
                    ]
                },
                open: true
            },
            access: {
                member: true,
                admin: true,
                author: true
            },
            data: {
                members: 30,
                responses: 23
            }
        }
    ], categories: {
        all: 23,
        senseit: 8,
        challenge: 9,
        spotit: 6
    }
};

backendRequests['api/projects'] = _projects;
backendRequests['api/project/101'] = _projects.list[0];
backendRequests['api/project/102'] = _projects.list[1];
backendRequests['api/project/103'] = _projects.list[2];
backendRequests['api/project/104'] = _projects.list[3];